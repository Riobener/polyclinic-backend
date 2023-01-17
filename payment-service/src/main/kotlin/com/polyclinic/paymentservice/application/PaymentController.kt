package com.polyclinic.paymentservice.application

import com.polyclinic.paymentservice.domain.entities.JpaPayment
import com.polyclinic.paymentservice.domain.entities.PaymentInputDto
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
        factory.setHost("host.docker.internal")
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

    @PostMapping("/markPaid/{applicationId}")
    fun markAsPaid(@RequestHeader(name = "User") userId: String, @PathVariable(name = "applicationId") applicationId: String): ResponseEntity<Any> {
        return ResponseEntity.ok(
            paymentService.findByApplicationId(UUID.fromString(applicationId))?.let {
                if(it.userId != UUID.fromString(userId))
                    return ResponseEntity.badRequest().body("Нет доступа к данному платежу")
                it.status = true
                val result = paymentService.save(it)
                sendPaymentReadyMessage(paymentId = it.id.toString())
                result
            }
        )
    }

    @PostMapping("/create")
    fun save(@RequestBody payment: PaymentInputDto): ResponseEntity<String> {
        return ResponseEntity.ok(
            paymentService.save(
                JpaPayment(
                    id = UUID.randomUUID(),
                    updateAt = Instant.now(),
                    status = false,
                    cost = (0..10000).random(),
                    userId = UUID.fromString(payment.userId),
                    applicationId = UUID.fromString(payment.applicationId)
                )
            ).id.toString()
        )
    }

    @GetMapping("/findAll")
    fun findAllById(@RequestHeader(name = "User") userId: String): ResponseEntity<Any>? {
        val result = paymentService.findAllByUserId(userId = UUID.fromString(userId))
        return ResponseEntity.ok(result)
    }
}

