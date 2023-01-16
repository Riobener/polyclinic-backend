package com.polyclinic.pacientservice.controllers

import com.polyclinic.pacientservice.domain.entities.Patient
import com.polyclinic.pacientservice.infrastructure.dto.PatientMedicalHistoryInputDto
import com.polyclinic.pacientservice.infrastructure.dto.PatientUpdateInputDto
import com.polyclinic.pacientservice.infrastructure.services.PatientService
import jakarta.servlet.http.HttpServletResponse
import org.apache.tomcat.util.http.fileupload.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.time.Instant
import java.util.*


@RestController

@RequestMapping("/patient")
class PatientController(
    @Autowired
    private val patientService: PatientService
) {

    @GetMapping("/health")
    fun checkHealth() = "All good"

    @GetMapping("/find/byAccountId/{id}")
    fun getPatient(@PathVariable id: String): ResponseEntity<Any> {
        return ResponseEntity.ok(patientService.findById(UUID.fromString(id)))
    }

    @GetMapping("/export/{id}")
    fun exportPatient(@PathVariable id: String,  response: HttpServletResponse) {
        patientService.findById(UUID.fromString(id))?.let { patient ->
            File("${patient.id}.txt").bufferedWriter().use { out ->
                out.write("ФИО:${patient.fio}\n")
                out.write("Адрес:${patient.address}\n")
                out.write("Телефон:${patient.phone}\n")
                out.write("День рождения:${patient.birthdayDate}\n")
                out.write("Пол:${patient.sex}\n")
                out.write("История посещений(Время и описание):\n")
                patient.medicalHistory.forEach {
                    out.write("${it}\n")
                }
            }
            response.contentType = "application/octet-stream"
            response.setHeader("Content-Disposition","attachment;filename="+"${patient.id}.txt")
            val inputStream: InputStream = FileInputStream(File("${patient.id}.txt"))
            IOUtils.copy(inputStream, response.outputStream)
            response.flushBuffer()
        }
    }

    @PostMapping("/update")
    fun updatePatient(@RequestBody patientUpdateInputDto: PatientUpdateInputDto): ResponseEntity<String> {
        patientService.updatePatient(patientUpdateInputDto)
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

    @PostMapping("/medical/history")
    fun assignMedicalHistory(@RequestBody medicalHistory: PatientMedicalHistoryInputDto):ResponseEntity<Any> {
        return ResponseEntity.ok(patientService.findById(UUID.fromString(medicalHistory.id))?.let{
            it.medicalHistory.add("${Instant.parse(medicalHistory.date)} ${medicalHistory.description}")
            patientService.savePatient(it)
        })
    }
}