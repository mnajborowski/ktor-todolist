package com.example.domain.model.user.dto

import com.example.domain.model.user.User
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: Int? = null,
    val nickname: String,
    val email: String
)

fun User.toUserDTO() = UserDTO(
    id = id.value,
    nickname = nickname,
    email = email
)

fun User.updateWith(other: UserDTO) {
    nickname = other.nickname
    email = other.email
}
