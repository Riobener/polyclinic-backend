package com.polyclinic.paymentservice.infrastructure.persistence

import com.polyclinic.paymentservice.domain.entities.JpaPayment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import java.util.UUID


@Repository
interface PaymentJpaRepository : JpaRepository<JpaPayment, UUID> {
    fun save(jpaPayment: JpaPayment): JpaPayment
}
