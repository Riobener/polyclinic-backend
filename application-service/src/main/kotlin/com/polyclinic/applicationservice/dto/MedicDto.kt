package com.polyclinic.applicationservice.dto

data class MedicTimeInputDto(
    val id: String,
    val appointmentTime: String,
)

data class PatientMedicalHistoryInputDto(
    val id: String,
    val date: String,
    val description: String,
)