package com.example.user.infrastructure.repository

import com.example.common.CityId
import com.example.user.domain.model.City
import com.example.user.domain.repository.CityRepository
import com.example.user.infrastructure.entity.CityEntity
import org.jetbrains.exposed.sql.transactions.transaction

class CityRepositoryImpl : CityRepository {
    override fun getById(id: CityId): City = transaction {
        CityEntity[id]
    }.toDomain()
}