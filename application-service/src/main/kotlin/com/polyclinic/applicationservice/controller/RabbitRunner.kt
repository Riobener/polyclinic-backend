package com.polyclinic.applicationservice.controller

import com.polyclinic.applicationservice.entity.ApplicationStatus
import com.polyclinic.applicationservice.repository.ApplicationRepository
import com.polyclinic.applicationservice.service.ApplicationService
import com.rabbitmq.client.*
import mu.KotlinLogging
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*


@Component
class RabbitRunner(
    private val applicationService: ApplicationService,
) : ApplicationRunner {
    private val logger = KotlinLogging.logger {}
    override fun run(args: ApplicationArguments?) {
/*        val QUEUE_NAME = "payment";
        val factory = ConnectionFactory()
        factory.setHost("host.docker.internal")
        val connection: Connection = factory.newConnection()
        val channel: Channel = connection.createChannel()
        channel.queueDeclare(
            QUEUE_NAME,
            false, false, false, null
        )
        logger.info("[!] Waiting for messages.!!!!!!!!!!!!!!!!!!!!!!!!!")

        val consumer: Consumer = object : DefaultConsumer(channel) {
            override fun handleDelivery(
                consumerTag: String,
                envelope: Envelope,
                properties: com.rabbitmq.client.AMQP.BasicProperties,
                body: ByteArray
            ) {
                val message = String(body, charset("UTF-8"))
                applicationService.findByPaymentId(UUID.fromString(message))?.let {
                    it.status = ApplicationStatus.CLOSED
                    it.updateAt = Instant.now()
                    applicationService.saveApplication(it)
                }
                logger.info("[x] Message Recieved' $message'")
            }
        }
        channel.basicConsume(QUEUE_NAME, true, consumer)*/
    }
}