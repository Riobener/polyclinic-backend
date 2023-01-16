package com.polyclinic.pacientservice.infrastructure.services

import com.polyclinic.pacientservice.domain.entities.Patient
import com.polyclinic.pacientservice.infrastructure.dto.PatientUpdateInputDto
import com.polyclinic.pacientservice.infrastructure.persistence.PatientJpaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.io.File
import java.util.*

@Service
class PatientService(
    @Autowired
    private val patientRepository: PatientJpaRepository
) {
    fun findById(id: UUID): Patient? {
        return patientRepository.findByIdOrNull(id)
    }

    fun updatePatient(patientUpdateInputDto: PatientUpdateInputDto): Patient? {
        return patientRepository.findByIdOrNull(UUID.fromString(patientUpdateInputDto.id))?.let {
            it.fio = patientUpdateInputDto.fio
            it.address = patientUpdateInputDto.address
            it.phone = patientUpdateInputDto.phone
            it.birthdayDate = patientUpdateInputDto.birthdayDate
            it.sex = patientUpdateInputDto.sex
            patientRepository.save(it)
        }
    }

    fun saveAllPatients(patients: List<Patient>) {
        patientRepository.saveAll(patients)
    }

    fun createFile(id: UUID) {
        patientRepository.findByIdOrNull(id)?.let { patient ->
            File("${patient.fio} Медкарта.txt").bufferedWriter().use { out ->
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
        }
    }

    fun savePatient(patient: Patient) {
        patientRepository.save(patient)
    }
}