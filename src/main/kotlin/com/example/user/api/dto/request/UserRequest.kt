package com.example.user.api.dto.request

import com.example.common.UserId
import com.example.user.domain.command.CreateUser
import com.example.user.domain.command.UpdateUser
import kotlinx.serialization.Serializable

@Serializable
data class UserRequest(
    val username: String,
    val email: String,
    val cityId: Int,
    val password: String? = null
) {
    fun toCreateUserCommand(): CreateUser = CreateUser(
        nickname = username,
        email = email,
        cityId = cityId,
        password = password!!
    )

    fun toUpdateUserCommand(id: UserId): UpdateUser = UpdateUser(
        id = id,
        nickname = username,
        email = email,
        cityId = cityId
    )
}
