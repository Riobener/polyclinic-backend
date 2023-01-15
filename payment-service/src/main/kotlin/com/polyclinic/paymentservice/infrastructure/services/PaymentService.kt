package com.polyclinic.paymentservice.infrastructure.services

import com.polyclinic.paymentservice.domain.entities.JpaPayment
import com.polyclinic.paymentservice.domain.repositories.PaymentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*


@Service
class PaymentService(
    @Autowired
    private val paymentRepository: PaymentRepository,
){

    fun findById(userId: String): JpaPayment? {
        return paymentRepository.findById(UUID.fromString(userId))
    }

    fun findByApplicationId(applicationId: String): JpaPayment? {
        return paymentRepository.findByApplicationId((applicationId))
    }

    fun markAsPaid(applicationId: String) {
        paymentRepository.save(JpaPayment(UUID.randomUUID(),applicationId,true))
    }

    fun save(jpaPayment: JpaPayment): JpaPayment {
        return paymentRepository.save(jpaPayment)
    }

}