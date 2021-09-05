package com.example.domain.model.user

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

typealias UserId = Int

object Users : IntIdTable("user") {
    val nickname = text("nickname")
    val email = text("email")
}

class User(
    id: EntityID<UserId>,
) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)
    var nickname by Users.nickname
    var email by Users.email
}