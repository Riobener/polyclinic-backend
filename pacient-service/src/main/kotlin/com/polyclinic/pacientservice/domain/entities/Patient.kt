package com.polyclinic.pacientservice.domain.entities



import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
data class Patient(
    @Id
    val id: UUID,
    val fio: String,
    val address:String,
    val phone:String,
    val birthdayDate: Instant,
    val sex:String,
    @ElementCollection
    @CollectionTable(name = "history",
        joinColumns = [JoinColumn(name = "account_id", referencedColumnName = "id")]
    )
    @MapKeyColumn(name = "date")
    @Column(name = "description")
    val medicalHistory:Map<Instant, String>
)
