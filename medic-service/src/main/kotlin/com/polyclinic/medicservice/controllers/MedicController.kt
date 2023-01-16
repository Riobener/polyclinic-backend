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

    @GetMapping("/find/all")
    fun getAllMedics():List<JpaMedic>{
        return medicService.findAllMedics()
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



}