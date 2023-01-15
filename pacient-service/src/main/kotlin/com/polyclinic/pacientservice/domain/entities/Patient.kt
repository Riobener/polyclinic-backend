package com.polyclinic.pacientservice.domain.entities



import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
data class Patient(
    @Id
    val id: UUID,
    val accountId: UUID,
    val fio: String,
    val address:String,
    val phone:String,
    val birthdayDate: Instant,
    val sex:String,
    @ElementCollection
    @CollectionTable(name = "medcart_date_mapping",
        joinColumns = [JoinColumn(name = "medcart_id", referencedColumnName = "id")]
    )
    @MapKeyColumn(name = "date")
    @Column(name = "description")
    val medicalHistory:Map<Instant, String>
)
