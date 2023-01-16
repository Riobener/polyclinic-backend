package com.polyclinic.medicservice.domain.entities

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
data class JpaMedic(
    @Id
    val id: UUID,
    val accountId: UUID,
    val fio: String,
    val availableTimeList: List<Instant>,
)
