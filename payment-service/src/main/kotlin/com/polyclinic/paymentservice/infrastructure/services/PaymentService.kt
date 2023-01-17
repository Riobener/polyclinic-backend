package com.polyclinic.paymentservice.infrastructure.services

import com.polyclinic.paymentservice.domain.entities.JpaPayment
import com.polyclinic.paymentservice.infrastructure.persistence.PaymentJpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*


@Service
class PaymentService(
    private val paymentRepository: PaymentJpaRepository,
){

    fun findAllByUserId(userId: UUID): List<JpaPayment> {
        return paymentRepository.findAllByUserId(userId)
    }

    fun findByUserId(userId: UUID): JpaPayment? {
        return paymentRepository.findByUserId(userId)
    }

    fun findByApplicationId(applicationId: UUID): JpaPayment? {
        return paymentRepository.findByApplicationId(applicationId)
    }

    fun save(jpaPayment: JpaPayment): JpaPayment {
        return paymentRepository.save(jpaPayment)
    }

}