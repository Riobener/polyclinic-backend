package com.polyclinic.medicservice.infrastructure.persistence

import com.polyclinic.medicservice.domain.entities.JpaMedic
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MedicJpaRepository :JpaRepository<JpaMedic, UUID> {
    // fun findById(id:UUID):JpaMedic?
    fun findByAccountId(accountId:UUID):JpaMedic?
    fun deleteByAccountId(accountId:UUID):JpaMedic?
    fun getAllMedics(medicId: UUID):List<JpaMedic>
}
