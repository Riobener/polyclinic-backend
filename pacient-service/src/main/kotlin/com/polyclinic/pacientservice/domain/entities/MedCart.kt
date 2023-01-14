package com.polyclinic.pacientservice.domain.entities

import jakarta.persistence.*
import java.util.Date
import java.util.UUID

@Embeddable
data class MedCart(
    @Id
    val id:UUID?,
    val address:String,
    val phone:String,
    val birthdayDate:Date,
    val sex:String,
    @ElementCollection
    @CollectionTable(name = "medcart_date_mapping",
        joinColumns = [JoinColumn(name = "medcart_id", referencedColumnName = "id")]
    )
    @MapKeyColumn(name = "date")
    @Column(name = "description")
    val medicalHistory:Map<Date, String>
)
