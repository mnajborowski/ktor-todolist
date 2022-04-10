package com.example.common.infrastructure.security.authorization

import com.example.common.infrastructure.security.principal.Role.READ
import com.example.common.infrastructure.security.principal.Role.WRITE
import com.example.common.infrastructure.security.principal.UserSession
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Application.configureAuthorizationRouting() {
    routing {
        authenticate("auth-form") {
            post("/login") {
                val userName = call.principal<UserIdPrincipal>()?.name.toString()
                call.sessions.set(UserSession(name = userName, roles = setOf(READ, WRITE)))
                call.respondRedirect("/hello")
            }
        }

        authenticate("auth-session-read") {
            get("/hello") {
                call.respondText { "Hello! You've logged in." }
            }
        }
    }
}