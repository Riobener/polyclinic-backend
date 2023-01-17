package com.polyclinic.paymentservice.domain.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.Instant
import java.util.*

@Entity
data class JpaPayment(
    @Id
    val id: UUID,
    val userId: UUID,
    val applicationId: UUID,
    var status: Boolean,
    val updateAt: Instant,
    val cost: Int,
)


data class PaymentInputDto(
    val applicationId: String,
    val userId: String,
)