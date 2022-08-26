package com.example.common.infrastructure.security.authorization

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
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
import kotlinx.html.label
import kotlinx.html.passwordInput
import kotlinx.html.postForm
import kotlinx.html.textInput
import java.util.*

fun Application.configureAuthorizationRouting() {
    routing {
        authenticate("auth-basic") {
            get("/login") {
                val userName = call.principal<UserIdPrincipal>()?.name.toString()
                call.sessions.set(UserSession(name = userName, roles = setOf(READ, WRITE)))
                call.respondRedirect("/hello")
            }
        }

        get("/login-form") {
            call.respondHtml {
                body {
                    postForm("/login") {
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
            post("/login-jwt") {
                val username = call.principal<UserIdPrincipal>()?.name.toString()
                val token = JWT.create()
                    .withAudience("http://0.0.0.0:8080/hello")
                    .withIssuer("http://0.0.0.0:8080/")
                    .withClaim("username", username)
                    .withExpiresAt(Date(System.currentTimeMillis() + 60000))
                    .sign(Algorithm.HMAC256("secret"))
                call.respond(hashMapOf("token" to token))
            }
        }

        authenticate("auth-form") {
            post("/login") {
                val username = call.principal<UserIdPrincipal>()?.name.toString()
                call.sessions.set(UserSession(name = username, roles = setOf(READ, WRITE)))
                call.respondRedirect("/hello")
            }
        }

        authenticate("auth-oauth-github") {
            get("/login-oauth") {}
        }

        get("/callback") {
            val principal = call.principal<OAuthAccessTokenResponse.OAuth2>()
            call.sessions.set(UserSession(name = principal?.accessToken.toString(), roles = setOf(READ, WRITE)))
            call.respondRedirect("/hello")
        }

        authenticate("auth-session-oauth") {
            get("/hello") {
                call.respondText { "Hello! You've logged in." }
            }
        }
    }
}