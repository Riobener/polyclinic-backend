package com.polyclinic.paymentservice.infrastructure.dto

import java.util.UUID
import kotlinx.serialization.Serializable

@Serializable
data class PaymentResponse(
    val id: UUID,
    val applicationId: String,
    val status: Boolean,
)

