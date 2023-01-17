package com.polyclinic.pacientservice.infrastructure.dto

import java.time.Instant
import java.util.*

data class PatientUpdateInputDto(
    val fio: String,
    val address:String,
    val phone:String,
    val birthdayDate: Instant,
    val sex:String,
)


data class PatientMedicalHistoryInputDto(
    val id: String,
    val date: String,
    val description: String,
)
