package com.example.domain.model.user.dto

import com.example.domain.model.user.User

data class UserDTO(
    val id: Int,
    val age: Int,
    val nickname: String
) {
    constructor(user: User) : this(
        id = user.id.value,
        age = user.age,
        nickname = user.nickname
    )
}

fun User.updateWith(other: UserDTO) {
    age = other.age
    nickname = other.nickname
}
