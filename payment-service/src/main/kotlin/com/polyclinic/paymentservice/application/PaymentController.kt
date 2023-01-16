package com.polyclinic.paymentservice.application

import com.polyclinic.paymentservice.domain.entities.JpaPayment
import com.polyclinic.paymentservice.infrastructure.services.PaymentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.*

@RestController

@RequestMapping("/payments")
class PaymentController(
    @Autowired
    private val paymentService: PaymentService,
) {
    @GetMapping("/health")
    fun checkHealth() = "All good"

    @PostMapping("/markPaid")
    fun markAsPaid(@RequestParam applicationId: UUID): ResponseEntity<*>? {
        val result = paymentService.markAsPaid(applicationId)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/create")
    fun save(): ResponseEntity<String> {
        return ResponseEntity.ok(
            paymentService.save(
                JpaPayment(
                    id = UUID.randomUUID(),
                    updateAt = Instant.now(),
                    status = false,
                    cost = (0..10000).random()
                )
            ).id.toString()
        )
    }

    @GetMapping("/byApplication")
    fun findByApplicationId(@RequestParam applicationId: UUID): ResponseEntity<*>? {
        val result = paymentService.findByApplicationId(applicationId)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/byId")
    fun findById(@RequestParam applicationId: UUID): ResponseEntity<*>? {
        val result = paymentService.findById(applicationId)
        return ResponseEntity.ok(result)
    }
}

