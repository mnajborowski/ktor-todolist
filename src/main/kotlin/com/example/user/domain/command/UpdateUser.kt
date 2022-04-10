package com.example.user.domain.command

import com.example.common.UserId

data class UpdateUser(
    val id: UserId,
    val nickname: String,
    val email: String,
    val cityId: Int,
)
