package com.polyclinic.applicationservice.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.polyclinic.applicationservice.dto.ApplicationCreationDto
import com.polyclinic.applicationservice.dto.ApplicationInputDto
import com.polyclinic.applicationservice.dto.ApplicationResponseDto
import com.polyclinic.applicationservice.dto.MedicTimeInputDto
import com.polyclinic.applicationservice.dto.mapper.toDto
import com.polyclinic.applicationservice.entity.ApplicationStatus
import com.polyclinic.applicationservice.entity.ApplicationType
import com.polyclinic.applicationservice.entity.JpaApplication
import com.polyclinic.applicationservice.service.ApplicationService
import mu.KotlinLogging
import org.json.JSONArray
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import java.time.Instant
import java.util.*


@RestController
@RequestMapping("/applications")
class ApplicationController(
    private val applicationService: ApplicationService,
) {
    @Autowired
    lateinit var restTemplate: RestTemplate

    @GetMapping("/health")
    fun checkHealth() = "All good"

    private val logger = KotlinLogging.logger {}

    fun getRoleFromJson(json: String): String{
        val role = JSONObject(json)
        val objTso = role.getJSONArray("roles")
        return objTso.getString(0)
    }

    @PostMapping("/")
    fun saveApplication(
        @RequestHeader(name = "User") userId: String,
        @RequestHeader(name = "roles") role: String,
        @RequestBody creationDto: ApplicationCreationDto
    ): ResponseEntity<ApplicationResponseDto> {
        check(getRoleFromJson(role) == "patient") { "Заявку может создать только пациент" }
        val body = MedicTimeInputDto(appointmentTime = creationDto.appointmentDate, id = creationDto.medicId.toString())
        val marshaled = ObjectMapper().writeValueAsString(body)
        val isMedicFree = restTemplate.postForEntity(
            "http://host.docker.internal:8083/medic/appointment",
            marshaled,
            Boolean::class.java
        ).body
        if(isMedicFree!!){
            return ResponseEntity.ok(
                applicationService.saveApplication(
                    JpaApplication(
                        id = UUID.randomUUID(),
                        patientId = UUID.fromString(userId),
                        medicId = creationDto.medicId,
                        appointmentDate = Instant.parse(creationDto.appointmentDate),
                        status = ApplicationStatus.IN_PROCESS,
                        type = ApplicationType.valueOf(creationDto.type),
                        updateAt = Instant.now(),
                    )
                ).toDto()
            )
        } else{
            return ResponseEntity.badRequest().body(null)
        }
    }

    @GetMapping("/byAccount")
    fun getApplicationsByAccountId(
        @RequestHeader(name = "User") userId: String,
        @RequestHeader(name = "Role") role: String,
    ): ResponseEntity<List<ApplicationResponseDto>> {
        if (role == "medic") {
            return ResponseEntity.ok(
                applicationService.findAllByMedicId(UUID.fromString(userId)).map { it.toDto() })
        } else {
            return ResponseEntity.ok(
                applicationService.findAllByPatientId(UUID.fromString(userId)).map { it.toDto() })
        }
    }

    @PostMapping("/assignTreatment")
    fun assignTreatmentById(
        @RequestHeader(name = "User") userId: String,
        @RequestBody applicationInput: ApplicationInputDto,
    ): ResponseEntity<ApplicationResponseDto> {
        return ResponseEntity.ok(applicationService.findById(UUID.fromString(applicationInput.id))?.let {
            val nextAppointmentTime = Instant.parse(applicationInput.nextAppointmentDate)
            check(it.medicId == UUID.fromString(userId)) { "Заявку может менять только врач, относящийся к ней непосредственно" }
            check(nextAppointmentTime.isAfter(Instant.now())) { "Время следующего посещения не может быть раньше текущего" }
            it.treatmentComment = applicationInput.treatmentComment
            it.directionComment = applicationInput.directionComment
            it.updateAt = Instant.now()
            it.status = ApplicationStatus.WAITING_FOR_REVISIT
            it.nextAppointmentDate = nextAppointmentTime
            applicationService.saveApplication(it)
        }?.toDto())
    }

    @PostMapping("/finish")
    fun finishApplicationAndAssignPayment(
        @RequestHeader(name = "User") userId: String,
        @RequestBody applicationInput: ApplicationInputDto,
    ): ResponseEntity<ApplicationResponseDto> {
        return ResponseEntity.ok(applicationService.findById(UUID.fromString(applicationInput.id))?.let {
            check(it.medicId == UUID.fromString(userId)) { "Заявку может завершить только врач, относящийся к ней непосредственно" }
            it.status = ApplicationStatus.WAITING_FOR_PAYMENT
            it.updateAt = Instant.now()
            //TODO it.paymentId = сервис создает payment и возвращает id
            applicationService.saveApplication(it)
        }?.toDto())
    }

    @PostMapping("/reject/{id}")
    fun rejectApplication(
        @RequestHeader(name = "User") userId: String,
        @PathVariable(name = "id") applicationId: String,
    ): ResponseEntity<ApplicationResponseDto> {
        return ResponseEntity.ok(
            applicationService.findById(UUID.fromString(applicationId))?.let {
                check(it.patientId == UUID.fromString(userId)) { "Заявку может отклонить только пациент" }
                it.status = ApplicationStatus.REJECTED
                it.updateAt = Instant.now()
                if (it.appointmentDate.isBefore(Instant.now())) {
                    val body = MedicTimeInputDto(appointmentTime = it.appointmentDate.toString(), id = it.medicId.toString())
                    val marshaled = ObjectMapper().writeValueAsString(body)
                    restTemplate.postForEntity(
                        "http://host.docker.internal:8083/medic/appointment/free",
                        marshaled,
                        Boolean::class.java
                    ).body
                }
                applicationService.saveApplication(it)
            }?.toDto()
        )
    }

    //Для сервиса платежей
    @PostMapping("/close/{id}")
    fun closeApplicationAfterPayment(
        @PathVariable(name = "id") paymentId: String,
    ): ResponseEntity<ApplicationResponseDto> {
        //TODO приходит из брокера после оплаты
        return ResponseEntity.ok(applicationService.findByPaymentId(UUID.fromString(paymentId))?.let {
            it.status = ApplicationStatus.CLOSED
            it.updateAt = Instant.now()
            applicationService.saveApplication(it)
        }?.toDto())
    }

    //Для другого сервиса
    @GetMapping("/byId/{id}")
    fun getApplicationByAccountId(
        @PathVariable(name = "id") applicationId: String,
    ): ResponseEntity<ApplicationResponseDto> {
        return ResponseEntity.ok(
            applicationService.findById(UUID.fromString(applicationId))?.toDto()
        )
    }

    //Для другого сервиса
    @GetMapping("/types")
    fun getApplicationTypes(): ResponseEntity<List<String>> {
        return ResponseEntity.ok(
            ApplicationType.values().map{it.name}
        )
    }

}