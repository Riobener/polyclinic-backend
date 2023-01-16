package com.polyclinic.medicservice.controllers

import com.polyclinic.medicservice.domain.entities.JpaMedic
import com.polyclinic.medicservice.infrastructure.dto.MedicDto
import com.polyclinic.medicservice.infrastructure.dto.MedicTimeInputDto
import com.polyclinic.medicservice.infrastructure.services.MedicService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.*

@RestController

@RequestMapping("/medic")
class MedicController (
    @Autowired
    private val medicService: MedicService
) {
    @GetMapping("/health")
    fun checkHealth() = "All good"

    @GetMapping("/find/all")
    fun getAllMedics():List<JpaMedic>{
        return medicService.findAllMedics().filter { it.availableTimeList.isNotEmpty() }
    }

    @GetMapping("/find/full/byAccountId/{accountId}")
    fun getFullMedic(@PathVariable accountId: UUID): JpaMedic? {
        return medicService.findByAccountId(accountId)
    }

    @GetMapping("/find/byAccountId/{accountId}")
    fun getMedic(@PathVariable accountId: UUID): MedicDto? {
        return medicService.findDtoMedicByAccountId(accountId)
    }

    @PostMapping("/update")
    fun updateMedic(@RequestBody medicDto: MedicDto): ResponseEntity<String> {
        medicService.updateMedic(medicDto)
        return ResponseEntity.ok()
            .body("Доктор успешно обновлён")
    }

    @PostMapping("/save/all")
    fun addAllMedics(@RequestBody medics:List<JpaMedic>): ResponseEntity<String> {
        medicService.saveAllMedics(medics)
        return ResponseEntity.ok()
            .body("Доктора успешно добавлены")
    }

    @PostMapping("/appointment/")
    fun updateTime(@RequestBody appointment: MedicTimeInputDto): ResponseEntity<Boolean> {
        medicService.findById(UUID.fromString(appointment.id))?.let{
            val time = Instant.parse(appointment.appointmentTime)
            if(it.availableTimeList.any{availableTime -> availableTime == time}){
                it.availableTimeList.remove(time)
                medicService.saveMedic(it)
                return ResponseEntity.ok(true)
            }
            return ResponseEntity.ok(false)
        }
        return ResponseEntity.ok(false)
    }



}