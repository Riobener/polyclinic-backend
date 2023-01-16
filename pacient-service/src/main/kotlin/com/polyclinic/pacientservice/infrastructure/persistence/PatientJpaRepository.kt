package com.polyclinic.pacientservice.infrastructure.persistence

import com.polyclinic.pacientservice.domain.entities.Patient
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*
@Repository
interface PatientJpaRepository :JpaRepository<Patient, UUID> {
    fun findByAccountId(accountId:UUID):Patient?
    fun deleteByAccountId(accountId:UUID):Patient?
}
