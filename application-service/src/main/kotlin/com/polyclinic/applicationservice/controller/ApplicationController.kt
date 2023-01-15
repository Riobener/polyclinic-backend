package com.polyclinic.applicationservice.controller

import com.polyclinic.applicationservice.dto.ApplicationCreationDto
import com.polyclinic.applicationservice.dto.ApplicationResponseDto
import com.polyclinic.applicationservice.dto.mapper.toDto
import com.polyclinic.applicationservice.entity.ApplicationStatus
import com.polyclinic.applicationservice.entity.ApplicationType
import com.polyclinic.applicationservice.entity.JpaApplication
import com.polyclinic.applicationservice.service.ApplicationService
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/applications")
class ApplicationController(
    private val applicationService: ApplicationService,
) {
    @GetMapping("/health")
    fun checkHealth() = "All good"

    @PostMapping("/")
    fun saveUser(@RequestHeader headers: HttpHeaders, @RequestBody creationDto: ApplicationCreationDto ): ResponseEntity<ApplicationResponseDto> {
        return ResponseEntity.ok(
            applicationService.saveApplication(
                JpaApplication(
                    id = UUID.randomUUID(),
                    patientId = UUID.fromString(headers.get("User")?.get(0)),
                    medicId = creationDto.medicId,
                    status = ApplicationStatus.IN_PROCESS,
                    type = ApplicationType.valueOf(creationDto.type),
                    updateAt = Instant.now(),
                )
            ).toDto()
        )
    }

    @GetMapping("/member/{id}")
    fun getApplicationsByMemberId(@PathVariable("id") id: String): ResponseEntity<List<ApplicationResponseDto>> {
        return ResponseEntity.ok(listOf())
    }
}