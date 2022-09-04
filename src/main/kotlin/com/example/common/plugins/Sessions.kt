package com.example.common.plugins

import com.example.common.infrastructure.security.principal.UserSession
import io.ktor.application.*
import io.ktor.sessions.*

fun Application.configureSessions() {
    install(Sessions) {
        cookie<UserSession>(
            name = "user_session",
            storage = SessionStorageMemory()
        ) {
            cookie.maxAgeInSeconds = 10
        }
    }
}