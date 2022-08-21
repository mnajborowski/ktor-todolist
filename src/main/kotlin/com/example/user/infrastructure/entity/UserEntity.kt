package com.example.user.infrastructure.entity

import com.example.common.UserId
import com.example.user.domain.model.User
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Users : IntIdTable("user") {
    val email = text("email")
    val city = reference("cityId", Cities.id)

    val username = text("username")
    val passwordHash = binary("password_hash")
    val salt = binary("salt")
}

class UserEntity(
    id: EntityID<UserId>,
) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(Users)
    var email by Users.email
    var city by CityEntity referencedOn Users.city

    var username by Users.username
    var passwordHash by Users.passwordHash
    var salt by Users.salt

    fun toDomain(): User = User(
        id = id.value,
        username = username,
        email = email,
        passwordHash = passwordHash,
        salt = salt,
        city = city.toDomain()
    )
}