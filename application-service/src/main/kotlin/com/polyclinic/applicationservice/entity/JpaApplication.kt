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
    val appointmentDate: Instant,
    @Enumerated(EnumType.STRING)
    var status: ApplicationStatus,
    @Enumerated(EnumType.STRING)
    val type: ApplicationType,
    var paymentId: UUID? = null,
    var treatmentComment: String? = null,
    var directionComment: String? = null,
    var nextAppointmentDate: Instant? = null,
    var updateAt: Instant,
)

enum class ApplicationStatus{
    IN_PROCESS,
    CLOSED,
    REJECTED,
    WAITING_FOR_REVISIT,
    WAITING_FOR_PAYMENT,
}

enum class ApplicationType{
    LOBOTOMY,
    MEDICAL_TESTS,
    COVID_19,
    LOCAL_DOCTOR,
}