package com.polyclinic.applicationservice.repository

import com.polyclinic.applicationservice.entity.JpaApplication
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface ApplicationRepository : JpaRepository<JpaApplication?, UUID?> {
    fun findById(id: UUID?): JpaApplication?
    fun findAllByPatientIdOrderByUpdateAtDesc(patientId: UUID): List<JpaApplication>
    fun findAllByMedicIdOrderByUpdateAtDesc(medicId: UUID): List<JpaApplication>
    fun findByPaymentId(paymentId: UUID): JpaApplication?
}