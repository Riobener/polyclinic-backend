package com.polyclinic.pacientservice.controllers
import com.polyclinic.pacientservice.domain.entities.Patient
import com.polyclinic.pacientservice.infrastructure.dto.PatientDto
import com.polyclinic.pacientservice.infrastructure.services.PatientService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.util.*

@RestController

@RequestMapping("/patient")
class PatientController(
    @Autowired
    private val patientService: PatientService
) {

    @GetMapping("/health")
    fun checkHealth() = "All good"

    @GetMapping("/find/full/byAccountId/{accountId}")
    fun getFullPatient(@PathVariable accountId: UUID): Patient? {
        return patientService.findByAccountId(accountId)
    }

    @GetMapping("/find/byAccountId/{accountId}")
    fun getPatient(@PathVariable accountId: UUID): PatientDto? {
        return patientService.findDtoPatientByAccountId(accountId)
    }

    @GetMapping("/export/{accountId}", produces = [MediaType.MULTIPART_MIXED_VALUE])
    fun exportPatient(@PathVariable accountId: UUID): ByteArray {
        patientService.createFile(accountId)
        val patient = patientService.findByAccountId(accountId)
        return File("${patient?.fio} Медкарта.txt").readBytes()
    }

    @PostMapping("/update")
    fun updatePatient(@RequestBody patientDto: PatientDto): ResponseEntity<String> {
        patientService.updatePatient(patientDto)
        return ResponseEntity.ok()
            .body("Пациент успешно обновлён")
    }

    @PostMapping("/save/all")
    fun saveAllPatients(@RequestBody patients:List<Patient>): ResponseEntity<String> {
        patientService.saveAllPatients(patients)
        return ResponseEntity.ok()
            .body("Пациенты успешно добавлены")
    }

    @PostMapping("/save")
    fun savePatient(@RequestBody patient: Patient):ResponseEntity<String> {
        patientService.savePatient(patient)
        return ResponseEntity.ok()
            .body("Пациент успешно добавлен")
    }

}