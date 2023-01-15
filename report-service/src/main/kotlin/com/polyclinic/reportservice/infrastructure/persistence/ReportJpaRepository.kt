package com.polyclinic.reportservice.infrastructure.persistence

import com.polyclinic.reportservice.domain.entities.JpaReport
import com.polyclinic.reportservice.domain.repositories.ReportRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface ReportJpaRepository : JpaRepository<JpaReport, UUID> {
    fun findId(id: String): JpaReport?
    fun findapplicationId(applicationId: String): JpaReport?
    fun getContent(content: String): JpaReport
}

@Component
class ReportRepositoryImpl(
    @Autowired
    private val jpaReportRepository: ReportJpaRepository
) : ReportRepository {
    override fun save(jpaReport: JpaReport): JpaReport {
        return jpaReportRepository.save(jpaReport)
    }

    override fun findId(id: String): JpaReport? {
        return jpaReportRepository.findId(id)
    }

    override fun findByApplicationId(applicationId: String): JpaReport? {
        return jpaReportRepository.findapplicationId(applicationId)
    }

    override fun getContent(content: String): JpaReport? {
        return jpaReportRepository.getContent(content)
    }
}