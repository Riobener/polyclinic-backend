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

    fun findById(id: UUID): JpaPayment? {
        return paymentRepository.findByIdOrNull(id)
    }

    fun markAsPaid(applicationId: UUID): JpaPayment? {
        return paymentRepository.findByIdOrNull(applicationId)?.let{
            it.status = true
            paymentRepository.save(it)
        }
    }

    fun save(jpaPayment: JpaPayment): JpaPayment {
        return paymentRepository.save(jpaPayment)
    }

}