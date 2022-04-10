package com.example.user.domain.command

data class CreateUser(
    val nickname: String,
    val email: String,
    val cityId: Int,
)