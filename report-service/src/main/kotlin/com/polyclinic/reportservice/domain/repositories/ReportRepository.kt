package com.polyclinic.reportservice.domain.repositories

import com.polyclinic.reportservice.domain.entities.JpaReport

interface ReportRepository {
    fun save(jpaReport: JpaReport): JpaReport
    fun findId(id: String): JpaReport?
    fun findByApplicationId(applicationId: String): JpaReport?
    fun getContent(content: String): JpaReport?
}