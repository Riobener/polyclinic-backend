package com.polyclinic.pacientservice.domain.entities



import jakarta.persistence.*
import java.util.UUID

@Entity
data class Patient(
    @Id
    val id: UUID?,
    val accountId: UUID,
    val fio: String,
    @Embedded
    val medCart: MedCart
)
