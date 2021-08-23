package com.example.domain.model.user

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Users : IntIdTable("user") {
    val age = integer("age")
    val nickname = text("nickname")
}

class User(
    id: EntityID<Int>,
) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)
    var age by Users.age
    var nickname by Users.nickname
}