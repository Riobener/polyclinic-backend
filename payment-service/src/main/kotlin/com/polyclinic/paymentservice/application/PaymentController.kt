package com.polyclinic.paymentservice.application

import com.polyclinic.paymentservice.domain.entities.JpaPayment
import com.polyclinic.paymentservice.infrastructure.services.PaymentService
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import mu.KotlinLogging
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
    private val QUEUE_NAME = "payment"

    private val logger = KotlinLogging.logger {}

    fun sendPaymentReadyMessage(paymentId: String) {
        val factory = ConnectionFactory()
        factory.setHost("localhost")
        val connection: Connection = factory.newConnection()
        val channel: Channel = connection.createChannel()
        channel.queueDeclare(
            QUEUE_NAME,
            false,
            false,
            false,
            null
        )
        val message = paymentId
        channel.basicPublish(
            "",
            QUEUE_NAME,
            null,
            message.toByteArray(charset("UTF-8"))
        )
        logger.info("[!] Sent '$message'")
        channel.close()
        connection.close()
    }

    @PostMapping("/health")
    fun checkHealth() = "All good"

    @PostMapping("/markPaid")
    fun markAsPaid(@RequestParam id: UUID): ResponseEntity<*>? {
        return ResponseEntity.ok(
            paymentService.markAsPaid(id)?.let {
                sendPaymentReadyMessage(paymentId = it.id.toString())
            })
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

    @GetMapping("/byId")
    fun findById(@RequestParam id: UUID): ResponseEntity<*>? {
        val result = paymentService.findById(id)
        return ResponseEntity.ok(result)
    }
}

