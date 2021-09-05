package com.example.domain.model.user

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Users : IntIdTable("user") {
    val nickname = text("nickname")
    val email = text("email")
}

class User(
    id: EntityID<Int>,
) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)
    var nickname by Users.nickname
    var email by Users.nickname
}