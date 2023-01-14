package com.polyclinic.pacientservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PacientServiceApplication

fun main(args: Array<String>) {
	runApplication<PacientServiceApplication>(*args)
}
