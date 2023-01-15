package com.polyclinic.applicationservice.entity

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import java.time.Instant
import java.util.*

@Entity
data class JpaApplication(
    @Id
    val id: UUID,
    val patientId: UUID,
    val medicId: UUID,
    @Enumerated(EnumType.STRING)
    val status: ApplicationStatus,
    @Enumerated(EnumType.STRING)
    val type: ApplicationType,
    val paymentId: UUID? = null,
    val treatmentComment: String? = null,
    val directionComment: String? = null,
    val updateAt: Instant,
)

enum class ApplicationStatus{
    IN_PROCESS,
    COMPLETED,
    REJECTED,
    WAITING_FOR_TESTS,
    WAITING_FOR_REVISIT,
    WAITING_FOR_PAYMENT,
}

enum class ApplicationType{
    LOBOTOMY,
    MEDICAL_TESTS,
    COVID_19,
    LOCAL_DOCTOR,
}