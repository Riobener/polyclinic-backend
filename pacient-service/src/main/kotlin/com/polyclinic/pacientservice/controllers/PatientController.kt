package com.polyclinic.pacientservice.controllers

import com.polyclinic.pacientservice.domain.entities.Patient
import com.polyclinic.pacientservice.infrastructure.services.PatientService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController

@RequestMapping("/patient")
class PatientController(
    @Autowired
    private val patientService: PatientService
) {


    @GetMapping("/{accountId}")
    fun getPatient(@PathVariable accountId: UUID): Patient? {
        return patientService.findByAccountId(accountId)
    }
}