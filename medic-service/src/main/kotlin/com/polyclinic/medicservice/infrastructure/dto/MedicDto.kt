package com.polyclinic.medicservice.infrastructure.dto

import java.time.Instant
import java.util.*

data class MedicDto(
    val id: UUID,
    val accountId: UUID,
    val fio: String,
    val availableTimeList: List<Instant>,
)
