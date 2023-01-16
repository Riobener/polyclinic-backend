package com.polyclinic.reportservice.application

import com.polyclinic.reportservice.domain.entities.JpaReport
import com.polyclinic.reportservice.infrastructure.sevices.ReportService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController

@RequestMapping("/api")
class ReportController(
        @Autowired
    private val reportService: ReportService,
) {
    @PostMapping("/report/getContent", "application/json")
    fun getContent(@RequestParam content: String): ResponseEntity<*>? {
        val result = reportService.getContent(content)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/report/save", "application/json")
    fun save(@RequestBody report : JpaReport): ResponseEntity<*>? {
        val result = reportService.save(report)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/report/findByApplicationId", "application/json")
    fun findByApplicationId(@RequestParam applicationId: String): ResponseEntity<*>? {
        val result = reportService.findByApplicationId(applicationId)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/report/findId", "application/json")
    fun findId(@RequestParam applicationId: String): ResponseEntity<*>? {
        val result = reportService.findId(applicationId)
        return ResponseEntity.ok(result)
    }
}