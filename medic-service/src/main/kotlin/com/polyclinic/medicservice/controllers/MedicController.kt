package com.polyclinic.medicservice.controllers

import com.polyclinic.medicservice.domain.entities.JpaMedic
import com.polyclinic.medicservice.infrastructure.dto.MedicDto
import com.polyclinic.medicservice.infrastructure.services.MedicService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController

@RequestMapping("/medic")
class MedicController (
    @Autowired
    private val medicService: MedicService
) {
    @GetMapping("/find/full/{accountId}")
    fun getFullMedic(@PathVariable accountId: UUID): JpaMedic? {
        return medicService.findByAccountId(accountId)
    }

    @GetMapping("/find/{accountId}")
    fun getPatient(@PathVariable accountId: UUID): MedicDto? {
        return medicService.findDtoPatientByAccountId(accountId)
    }

    @PostMapping("/update")
    fun updateMedic(@RequestBody medicDto: MedicDto): ResponseEntity<String> {
        medicService.updateMedic(medicDto)
        return ResponseEntity.ok()
            .body("Доктор успешно обновлён")
    }
}