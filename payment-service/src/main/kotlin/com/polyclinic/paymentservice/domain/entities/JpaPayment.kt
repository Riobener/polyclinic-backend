package com.polyclinic.paymentservice.domain.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.*

@Entity
data class JpaPayment(
    @Id
    val id: UUID?,
    val applicationId: UUID,
    val status: Boolean,
) {

}