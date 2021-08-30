package com.example.domain.model.user.dto

import com.example.domain.model.user.User
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: Int? = null,
    val age: Int,
    val nickname: String
)

fun User.toUserDTO() = UserDTO(
    id = id.value,
    age = age,
    nickname = nickname
)

fun User.updateWith(other: UserDTO) {
    age = other.age
    nickname = other.nickname
}
