package com.example.user.domain.model

import com.example.common.UserId
import com.example.user.api.dto.response.CityResponse
import com.example.user.api.dto.response.UserResponse

data class User(
    val id: UserId? = null,
    val email: String,
    val city: City,

    val username: String,
    val passwordHash: ByteArray = byteArrayOf(),
    val salt: ByteArray = byteArrayOf(),
) {
    fun toUserResponse(): UserResponse = UserResponse(
        id = id,
        username = username,
        email = email,
        city = CityResponse(city.id, city.name)
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (email != other.email) return false
        if (city != other.city) return false
        if (username != other.username) return false
        if (!passwordHash.contentEquals(other.passwordHash)) return false
        if (!salt.contentEquals(other.salt)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + email.hashCode()
        result = 31 * result + city.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + passwordHash.contentHashCode()
        result = 31 * result + salt.contentHashCode()
        return result
    }
}