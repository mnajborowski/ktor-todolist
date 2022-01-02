package com.example.plugins

import com.example.infrastructure.security.principal.UserSession
import io.ktor.application.*
import io.ktor.sessions.*

fun Application.configureSessions() {
    install(Sessions) {
        cookie<UserSession>("user_session", SessionStorageMemory())
    }
}