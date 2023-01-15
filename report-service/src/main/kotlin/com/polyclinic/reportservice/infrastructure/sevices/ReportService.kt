package com.polyclinic.reportservice.infrastructure.sevices

import com.polyclinic.reportservice.domain.entities.JpaReport
import com.polyclinic.reportservice.domain.repositories.ReportRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ReportService(
        @Autowired
        private val reportRepository: ReportRepository,
){
        fun save(jpaReport: JpaReport): JpaReport {
                return reportRepository.save(jpaReport)
        }

        fun findId(id: String): JpaReport? {
                return reportRepository.findId(id)
        }

        fun findByApplicationId(applicationId: String): JpaReport? {
                return reportRepository.findByApplicationId(applicationId)
        }

        fun getContent(content: String): JpaReport? {
                return reportRepository.getContent(content)
        }
}