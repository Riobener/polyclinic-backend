package com.polyclinic.paymentservice.domain.repositories

import com.polyclinic.paymentservice.domain.entities.JpaPayment
import java.util.UUID

interface PaymentRepository {
    fun save(jpaPayment: JpaPayment): JpaPayment
    fun findById(id: UUID): JpaPayment?
    fun findByApplicationId(applicationId: UUID): JpaPayment?
}