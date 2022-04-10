package com.example.user.api.dto.request

import com.example.common.UserId
import com.example.user.domain.command.CreateUser
import com.example.user.domain.command.UpdateUser
import kotlinx.serialization.Serializable

@Serializable
data class UserRequest(
    val nickname: String,
    val email: String,
    val cityId: Int,
) {
    fun toCreateUserCommand(): CreateUser = CreateUser(
        nickname = nickname,
        email = email,
        cityId = cityId
    )

    fun toUpdateUserCommand(id: UserId): UpdateUser = UpdateUser(
        id = id,
        nickname = nickname,
        email = email,
        cityId = cityId
    )
}
