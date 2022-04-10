package com.example.user.domain.model

import com.example.common.UserId
import com.example.user.api.dto.response.CityResponse
import com.example.user.api.dto.response.UserResponse

data class User(
    val id: UserId? = null,
    val nickname: String,
    val email: String,
    val city: City,
) {
    fun toUserResponse(): UserResponse = UserResponse(
        id = id,
        nickname = nickname,
        email = email,
        city = CityResponse(city.id, city.name)
    )
}