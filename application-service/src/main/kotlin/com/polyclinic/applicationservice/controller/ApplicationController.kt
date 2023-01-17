package com.polyclinic.applicationservice.controller

import com.polyclinic.applicationservice.dto.ApplicationCreationDto
import com.polyclinic.applicationservice.dto.ApplicationInputDto
import com.polyclinic.applicationservice.dto.MedicTimeInputDto
import com.polyclinic.applicationservice.dto.PatientMedicalHistoryInputDto
import com.polyclinic.applicationservice.entity.ApplicationStatus
import com.polyclinic.applicationservice.entity.ApplicationType
import com.polyclinic.applicationservice.entity.JpaApplication
import com.polyclinic.applicationservice.service.ApplicationService
import mu.KotlinLogging
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.*
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
    ): ResponseEntity<Any> {
        if(getRoleFromJson(role) != "patient")
            return ResponseEntity.badRequest().body("Заявку может создать только пациент")
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.accept = Arrays.asList(MediaType.APPLICATION_JSON)
        val entity: HttpEntity<MedicTimeInputDto> = HttpEntity<MedicTimeInputDto>(MedicTimeInputDto(appointmentTime = creationDto.appointmentDate, id = creationDto.medicId.toString()), headers)
        val isMedicFree = restTemplate.exchange("http://localhost:8083/medic/appointment", HttpMethod.POST, entity, Boolean::class.java).body
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
                )
            )
        } else{
            return ResponseEntity.badRequest().body(null)
        }
    }

    @GetMapping("/byAccount")
    fun getApplicationsByAccountId(
        @RequestHeader(name = "User") userId: String,
        @RequestHeader(name = "roles") role: String,
    ): ResponseEntity<List<JpaApplication>> {
        if (getRoleFromJson(role) == "medic") {
            return ResponseEntity.ok(
                applicationService.findAllByMedicId(UUID.fromString(userId)))
        } else {
            return ResponseEntity.ok(
                applicationService.findAllByPatientId(UUID.fromString(userId)))
        }
    }

    @PostMapping("/assignTreatment")
    fun assignTreatmentById(
        @RequestHeader(name = "User") userId: String,
        @RequestBody applicationInput: ApplicationInputDto,
    ): ResponseEntity<Any> {
        return ResponseEntity.ok(applicationService.findById(UUID.fromString(applicationInput.id))?.let {
            val nextAppointmentTime = Instant.parse(applicationInput.nextAppointmentDate)
            if(it.medicId != UUID.fromString(userId))
                return ResponseEntity.badRequest().body("Заявку может менять только врач, относящийся к ней непосредственно")
            if(nextAppointmentTime.isBefore(Instant.now()))
                return ResponseEntity.badRequest().body("Время следующего посещения не может быть раньше текущего")
            it.treatmentComment = applicationInput.treatmentComment
            it.directionComment = applicationInput.directionComment
            it.diagnosisComment = applicationInput.diagnosisComment
            it.updateAt = Instant.now()
            it.status = ApplicationStatus.WAITING_FOR_REVISIT
            it.nextAppointmentDate = nextAppointmentTime
            applicationService.saveApplication(it)
        })
    }

    @PostMapping("/finish/{id}")
    fun finishApplicationAndAssignPayment(
        @RequestHeader(name = "User") userId: String,
        @PathVariable(name = "id") applicationId: String,
    ): ResponseEntity<Any> {
        return ResponseEntity.ok(applicationService.findById(UUID.fromString(applicationId))?.let {
            if(it.medicId != UUID.fromString(userId))
                return ResponseEntity.badRequest().body("Заявку может завершить только врач, относящийся к ней непосредственно")
            it.status = ApplicationStatus.WAITING_FOR_PAYMENT
            it.updateAt = Instant.now()
            val headers = HttpHeaders()
            headers.contentType = MediaType.APPLICATION_JSON
            headers.accept = Arrays.asList(MediaType.APPLICATION_JSON)
            val entity: HttpEntity<MedicTimeInputDto> = HttpEntity<MedicTimeInputDto>(MedicTimeInputDto(appointmentTime = it.appointmentDate.toString(), id = it.medicId.toString()), headers)
            val paymentId = restTemplate.exchange("http://localhost:8085/payments/create", HttpMethod.POST, entity, String::class.java).body
            val entityTwo: HttpEntity<PatientMedicalHistoryInputDto> = HttpEntity<PatientMedicalHistoryInputDto>(PatientMedicalHistoryInputDto(id = it.patientId.toString(), date = it.appointmentDate.toString(), description = it.diagnosisComment!!), headers)
            restTemplate.exchange("http://localhost:8082/patient/medical/history", HttpMethod.POST, entityTwo, String::class.java)
            it.paymentId = UUID.fromString(paymentId)
            applicationService.saveApplication(it)
        })
    }

    @PostMapping("/reject/{id}")
    fun rejectApplication(
        @RequestHeader(name = "User") userId: String,
        @PathVariable(name = "id") applicationId: String,
    ): ResponseEntity<Any> {
        return ResponseEntity.ok(
            applicationService.findById(UUID.fromString(applicationId))?.let {
                if(it.patientId != UUID.fromString(userId))
                    return ResponseEntity.badRequest().body("Заявку может отклонить только пациент")
                it.status = ApplicationStatus.REJECTED
                it.updateAt = Instant.now()
                if (it.appointmentDate.isAfter(Instant.now())) {
                    val headers = HttpHeaders()
                    headers.contentType = MediaType.APPLICATION_JSON
                    headers.accept = Arrays.asList(MediaType.APPLICATION_JSON)
                    val entity: HttpEntity<MedicTimeInputDto> = HttpEntity<MedicTimeInputDto>(MedicTimeInputDto(appointmentTime = it.appointmentDate.toString(), id = it.medicId.toString()), headers)
                    restTemplate.exchange("http://localhost:8083/medic/appointment/free", HttpMethod.POST, entity, Boolean::class.java).body
                }
                applicationService.saveApplication(it)
            }
        )
    }

    //Для другого сервиса
    @GetMapping("/byId/{id}")
    fun getApplicationByAccountId(
        @PathVariable(name = "id") applicationId: String,
    ): ResponseEntity<Any> {
        return ResponseEntity.ok(
            applicationService.findById(UUID.fromString(applicationId))
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