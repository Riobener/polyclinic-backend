package com.polyclinic.applicationservice.dto.mapper

import com.polyclinic.applicationservice.dto.ApplicationResponseDto
import com.polyclinic.applicationservice.entity.JpaApplication
import java.time.Instant
import java.util.*

fun JpaApplication.toDto() = ApplicationResponseDto(
    patientId = patientId,
    medicId = medicId,
    status = status.name,
    type = type.name,
    paymentId = paymentId,
    treatmentComment = treatmentComment,
    directionComment = directionComment,
    updateAt = updateAt,
)
