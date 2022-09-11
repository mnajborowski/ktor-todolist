package com.example.common.plugins.authorization

import com.example.common.CityId
import com.example.common.infrastructure.security.principal.UserSession
import com.example.user.domain.service.UserService
import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.sessions.*
import org.koin.ktor.ext.inject
import javax.naming.AuthenticationException

fun Route.livesInCity(
    cityId: CityId,
    build: Route.() -> Unit
): Route {
    val service: UserService by inject()
    return authorize(listOf("auth-lives-in-city"), build) {
        val session = call.sessions.get<UserSession>()
            ?: throw AuthenticationException("Session not found")
        val user = service.getUser(session.name)
        if (user.city.id != cityId)
            throw SecurityException(
                "User ${user.username} doesn't live in ${user.city.name}."
            )
    }
}