package com.polyclinic.medicservice.infrastructure.services

import com.polyclinic.medicservice.domain.entities.JpaMedic
import com.polyclinic.medicservice.infrastructure.dto.MedicDto
import com.polyclinic.medicservice.infrastructure.persistence.MedicJpaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.io.File
import java.util.*

@Service
class MedicService (
    @Autowired
    private val medicRepository: MedicJpaRepository
){

    fun findByAccountId(accountId:UUID): JpaMedic? {
        return medicRepository.findByAccountId(accountId)
    }

    fun findById(id:UUID): JpaMedic? {
        return medicRepository.findByIdOrNull(id)
    }

    fun findAllMedics(): List<JpaMedic> =
        medicRepository.findAll()

    fun findDtoMedicByAccountId(accountId: UUID):MedicDto?{
        val medic = medicRepository.findByAccountId(accountId)
        if (medic != null) {
            return MedicDto(
                medic.accountId,
                medic.fio,
                medic.availableTimeList)
        }
        return null
    }

    fun saveAllMedics(medics:List<JpaMedic>){
        medicRepository.saveAll(medics)
    }

    fun updateMedic(medicDto: MedicDto){
        val medic = medicRepository.findByAccountId(medicDto.accountId)
        medicRepository.delete(medic!!)
        medicRepository.save(JpaMedic(
            id = medic.id,
            accountId = medicDto.accountId,
            fio = medicDto.fio,
            availableTimeList = medicDto.availableTimeList.toMutableList()))
    }

}