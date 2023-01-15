package com.polyclinic.reportservice.infrastructure.dto

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class ReportResponse(
        val id: UUID?,
        val content: String,
        val applicationId : String
)