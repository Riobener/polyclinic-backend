package com.polyclinic.medicservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MedicServiceApplication

fun main(args: Array<String>) {
	runApplication<MedicServiceApplication>(*args)
}
