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
    @ElementCollection
    @CollectionTable(name = "history",
        joinColumns = [JoinColumn(name = "account_id", referencedColumnName = "id")]
    )
    @MapKeyColumn(name = "date")
    @Column(name = "AvailableTime")
    val availableTimeList: List<Instant>,
)
