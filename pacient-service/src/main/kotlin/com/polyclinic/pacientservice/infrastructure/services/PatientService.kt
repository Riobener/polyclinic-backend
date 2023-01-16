package com.polyclinic.pacientservice.infrastructure.services

import com.polyclinic.pacientservice.domain.entities.Patient
import com.polyclinic.pacientservice.infrastructure.dto.PatientDto
import com.polyclinic.pacientservice.infrastructure.persistence.PatientJpaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.File
import java.util.*

@Service
class PatientService (
    @Autowired
    private val patientRepository: PatientJpaRepository
){
    fun findByAccountId(accountId:UUID): Patient? {
        return patientRepository.findByAccountId(accountId)
    }

    fun findDtoPatientByAccountId(accountId: UUID):PatientDto?{
        val patient = patientRepository.findByAccountId(accountId)
        if (patient != null) {
            return PatientDto(accountId,
                patient.fio,
                patient.address,
                patient.phone,
                patient.birthdayDate,
                patient.sex,
                patient.medicalHistory)
        }
        return null
    }

    fun updatePatient(patientDto: PatientDto){
        val patient = patientRepository.findByAccountId(patientDto.accountId)
        patientRepository.delete(patient!!)
        patientRepository.save(Patient(
            id = patient.id,
            accountId = patientDto.accountId,
            fio = patientDto.fio,
            address = patientDto.address,
            phone = patientDto.phone,
            birthdayDate = patientDto.birthdayDate,
            sex = patientDto.sex,
            medicalHistory = patientDto.medicalHistory))
    }

    fun createFile(accountId: UUID){
        val patient = patientRepository.findByAccountId(accountId)
        File("${patient?.fio} Медкарта.txt").bufferedWriter().use { out ->
            if (patient != null) {
                out.write("ФИО:${patient.fio}\n")
                out.write("Адрес:${patient.address}\n")
                out.write("Телефон:${patient.phone}\n")
                out.write("День рождения:${patient.birthdayDate}\n")
                out.write("Пол:${patient.sex}\n")
                out.write("История посещений(Время и описание):\n")
                patient.medicalHistory.forEach{
                    out.write("${it.key}:${it.value}\n")
                }
            }
        }
    }
}