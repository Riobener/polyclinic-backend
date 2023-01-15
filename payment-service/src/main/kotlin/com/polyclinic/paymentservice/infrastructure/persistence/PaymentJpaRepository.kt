package com.polyclinic.paymentservice.infrastructure.persistence

import com.polyclinic.paymentservice.domain.entities.JpaPayment
import com.polyclinic.paymentservice.domain.repositories.PaymentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import java.util.UUID


@Repository
interface PaymentJpaRepository : JpaRepository<JpaPayment, UUID> {
    //fun findById(id: UUID): JpaPayment?
    fun findByApplicationId(applicationId: String): JpaPayment?
    fun save(jpaPayment: JpaPayment): JpaPayment
}

@Component
class PaymentRepositoryImpl(
    @Autowired
    private val jpaPaymentRepository: PaymentJpaRepository
) : PaymentRepository {
    override fun save(jpaPayment: JpaPayment): JpaPayment {
        return jpaPaymentRepository.save(jpaPayment)
    }

    override fun findById(id: UUID): JpaPayment? {
        return jpaPaymentRepository.findById(id).orElse(null)
    }

    override fun findByApplicationId(applicationId: String): JpaPayment? {
        return jpaPaymentRepository.findByApplicationId(applicationId)
    }
}
