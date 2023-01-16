package com.polyclinic.applicationservice.controller

import com.polyclinic.applicationservice.dto.ApplicationCreationDto
import com.polyclinic.applicationservice.dto.ApplicationInputDto
import com.polyclinic.applicationservice.dto.ApplicationResponseDto
import com.polyclinic.applicationservice.dto.mapper.toDto
import com.polyclinic.applicationservice.entity.ApplicationStatus
import com.polyclinic.applicationservice.entity.ApplicationType
import com.polyclinic.applicationservice.entity.JpaApplication
import com.polyclinic.applicationservice.service.ApplicationService
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/applications")
class ApplicationController(
    private val applicationService: ApplicationService,
) {
    @GetMapping("/health")
    fun checkHealth() = "All good"

    @PostMapping("/")
    fun saveApplication(
        @RequestHeader(name = "User") userId: String,
        @RequestHeader(name = "Role") role: String,
        @RequestBody creationDto: ApplicationCreationDto
    ): ResponseEntity<ApplicationResponseDto> {
        check(role == "patient") { "Заявку может создать только пациент" }
        //TODO проверка сервиса врача на доступность времени
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

    @GetMapping("/assignTreatment")
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

    @GetMapping("/finish")
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

    //Для другого сервиса
    @GetMapping("/reject/{id}")
    fun rejectApplication(
        @RequestHeader(name = "User") userId: String,
        @RequestParam(name = "id") applicationId: String,
    ): ResponseEntity<ApplicationResponseDto> {
        return ResponseEntity.ok(
            applicationService.findById(UUID.fromString(applicationId))?.let{
                check(it.patientId == UUID.fromString(userId)){ "Заявку может отклонить только пациент" }
                it.status = ApplicationStatus.REJECTED
                it.updateAt = Instant.now()
                if(it.appointmentDate.isBefore(Instant.now())){
                    //TODO Добавить время врачу, в случае отклонения (запрос к сервису)
                }
                applicationService.saveApplication(it)
            }?.toDto()
        )
    }

    //Для сервиса платежей
    @GetMapping("/close/{id}")
    fun closeApplicationAfterPayment(
        @RequestParam(name = "id") paymentId: String,
    ): ResponseEntity<ApplicationResponseDto> {
        //TODO проверка на то, что payment оплачен (запрос к сервису)
        return ResponseEntity.ok(applicationService.findByPaymentId(UUID.fromString(paymentId))?.let {
            it.status = ApplicationStatus.CLOSED
            it.updateAt = Instant.now()
            applicationService.saveApplication(it)
        }?.toDto())
    }

    //Для другого сервиса
    @GetMapping("/byId/{id}")
    fun getApplicationByAccountId(
        @RequestParam(name = "id") applicationId: String,
    ): ResponseEntity<ApplicationResponseDto> {
        return ResponseEntity.ok(
            applicationService.findById(UUID.fromString(applicationId))?.toDto()
        )
    }

}