package com.polyclinic.paymentservice.application

import com.polyclinic.paymentservice.domain.entities.JpaPayment
import com.polyclinic.paymentservice.infrastructure.services.PaymentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController

@RequestMapping("/api")
class PaymentController(
    @Autowired
    private val paymentService: PaymentService,
) {
    @GetMapping("/health")
    fun checkHealth() = "All good"

    @PostMapping("/payments/markPaid")
    fun markAsPaid(@RequestParam applicationId: UUID): ResponseEntity<*>? {
        val result = paymentService.markAsPaid(applicationId)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/payments/save")
    fun save(@RequestBody payment: JpaPayment): ResponseEntity<*>? {
        paymentService.save(payment)
        return ResponseEntity.ok()
            .body("Платёж успешно сохранён")
    }

    @GetMapping("/payments/byApplication")
    fun findByApplicationId(@RequestParam applicationId: UUID): ResponseEntity<*>? {
        val result = paymentService.findByApplicationId(applicationId)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/payments/byId")
    fun findById(@RequestParam applicationId: UUID): ResponseEntity<*>? {
        val result = paymentService.findById(applicationId)
        return ResponseEntity.ok(result)
    }
}

