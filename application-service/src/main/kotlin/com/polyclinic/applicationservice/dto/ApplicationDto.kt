package com.polyclinic.applicationservice.dto

import java.time.Instant
import java.util.*

data class ApplicationCreationDto(
    val medicId: UUID,
    val type: String,
    val appointmentDate: String,
)

data class ApplicationInputDto(
    val id: String,
    val treatmentComment: String,
    val directionComment: String,
    val nextAppointmentDate: String,
)

data class ApplicationResponseDto(
    val id: UUID,
    val patientId: UUID,
    val medicId: UUID,
    val status: String,
    val type: String,
    val paymentId: UUID?,
    val treatmentComment: String?,
    val directionComment: String?,
    val updateAt: Instant,
)

enum class ApplicationTypeDto{
    LOBOTOMY,
    MEDICAL_TESTS,
    COVID_19,
    LOCAL_DOCTOR,
}