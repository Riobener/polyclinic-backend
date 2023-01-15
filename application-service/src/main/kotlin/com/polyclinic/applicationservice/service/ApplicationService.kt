package com.polyclinic.applicationservice.service

import com.polyclinic.applicationservice.entity.JpaApplication
import com.polyclinic.applicationservice.repository.ApplicationRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class ApplicationService(
    private val applicationRepository: ApplicationRepository,
) {
    fun saveApplication(jpaApplication: JpaApplication): JpaApplication =
        applicationRepository.save(jpaApplication)

    fun findById(id: UUID): JpaApplication? =
        applicationRepository.findByIdOrNull(id)

    fun findAllByPatientId(patientId: UUID): List<JpaApplication> =
        applicationRepository.findAllByPatientIdOrderByUpdateAtDesc(patientId)

    fun findAllByMedicId(medicId: UUID): List<JpaApplication> =
        applicationRepository.findAllByMedicIdOrderByUpdateAtDesc(medicId)

    fun findByPaymentId(paymentId: UUID): JpaApplication? =
        applicationRepository.findByPaymentId(paymentId)
}

