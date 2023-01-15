package com.polyclinic.applicationservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApplicationServiceApplication

fun main(args: Array<String>) {
	runApplication<ApplicationServiceApplication>(*args)
}
