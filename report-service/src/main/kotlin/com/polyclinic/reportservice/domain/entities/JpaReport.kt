package com.polyclinic.reportservice.domain.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id

import java.util.*

@Entity
data class JpaReport(
        @Id
        val id: UUID?,
        val content: String,
        val applicationId : String
)