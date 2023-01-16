package com.polyclinic.pacientservice.domain.entities


import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
data class Patient(
    @Id
    val id: UUID,
    var fio: String,
    var address: String,
    var phone: String,
    var birthdayDate: Instant,
    var sex: String,
    @ElementCollection
    var medicalHistory: MutableList<String>
)
