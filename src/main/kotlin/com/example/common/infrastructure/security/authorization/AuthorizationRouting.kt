package com.example.common.infrastructure.security.authorization

import com.example.common.infrastructure.security.principal.Role.READ
import com.example.common.infrastructure.security.principal.Role.WRITE
import com.example.common.infrastructure.security.principal.UserSession
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.html.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import kotlinx.html.body
import kotlinx.html.br
import kotlinx.html.button
import kotlinx.html.getForm
import kotlinx.html.label
import kotlinx.html.passwordInput
import kotlinx.html.textInput

fun Application.configureAuthorizationRouting() {
    routing {
        get("/login-form") {
            call.respondHtml {
                body {
                    getForm("/login") {
                        label { text("Username") }
                        br
                        textInput(name = "username")
                        br
                        br
                        label { text("Password") }
                        br
                        passwordInput(name = "password")
                        br
                        button { text("Login") }
                    }
                }
            }
        }

        authenticate("auth-form") {
            get("/login") {
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