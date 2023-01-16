package com.polyclinic.pacientservice.infrastructure.dto

import java.time.Instant
import java.util.*

data class PatientDto(
    val accountId: UUID,
    val fio: String,
    val address:String,
    val phone:String,
    val birthdayDate: Instant,
    val sex:String,
    val medicalHistory:Map<Instant, String>
)
