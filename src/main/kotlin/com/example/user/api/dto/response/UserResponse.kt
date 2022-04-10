package com.example.user.api.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: Int? = null,
    val nickname: String,
    val email: String,
    val city: CityResponse,
)