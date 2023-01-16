package com.polyclinic.paymentservice.domain.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.Instant
import java.util.*

@Entity
data class JpaPayment(
    @Id
    val id: UUID?,
    val status: Boolean,
    val updateAt: Instant,
    val cost: Int,
)