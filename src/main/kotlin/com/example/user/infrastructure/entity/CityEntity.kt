package com.example.user.infrastructure.entity

import com.example.common.CityId
import com.example.user.domain.model.City
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Cities : IntIdTable("city") {
    var name = text("name")
}

class CityEntity(
    id: EntityID<CityId>,
) : IntEntity(id) {
    companion object : IntEntityClass<CityEntity>(Cities)

    var name by Cities.name

    fun toDomain(): City = City(
        id = id.value,
        name = name
    )
}