package com.example.common.plugins.authorization

import com.example.common.infrastructure.security.principal.Role
import com.example.common.infrastructure.security.principal.UserSession
import com.example.common.infrastructure.security.principal.getConfigurationNames
import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.sessions.*
import javax.naming.AuthenticationException

fun Route.requireRole(
    vararg roles: Role,
    build: Route.() -> Unit
): Route {
    require(roles.isNotEmpty()) {
        "At least one role need to be provided"
    }
    return authorize(roles.toSet().getConfigurationNames(), build) {
        val session = call.sessions.get<UserSession>()
            ?: throw AuthenticationException("Session not found")
        if (session.roles.none { it in roles }) {
            throw SecurityException(
                "User ${session.name} doesn't have any of $roles."
            )
        }
    }
}