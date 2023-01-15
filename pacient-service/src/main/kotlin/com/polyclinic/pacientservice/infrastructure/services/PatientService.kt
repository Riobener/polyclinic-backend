package com.polyclinic.pacientservice.infrastructure.services

import com.polyclinic.pacientservice.domain.entities.Patient
import com.polyclinic.pacientservice.infrastructure.persistence.PatientJpaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class PatientService (
    @Autowired
    private val patientRepository: PatientJpaRepository
){
    fun findByAccountId(accountId:UUID): Patient? {
        return patientRepository.findByAccountId(accountId)
    }
}