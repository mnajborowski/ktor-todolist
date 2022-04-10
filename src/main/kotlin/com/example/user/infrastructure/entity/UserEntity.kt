package com.example.user.infrastructure.entity

import com.example.common.UserId
import com.example.user.domain.model.User
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Users : IntIdTable("user") {
    val nickname = text("nickname")
    val email = text("email")
    val city = reference("cityId", Cities.id)
}

class UserEntity(
    id: EntityID<UserId>,
) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(Users)
    var nickname by Users.nickname
    var email by Users.email
    var city by CityEntity referencedOn Users.city

    fun toDomain(): User = User(
        id = id.value,
        nickname = nickname,
        email = email,
        city = city.toDomain()
    )
}