package com.example.common.plugins

import com.example.common.infrastructure.security.principal.Role.READ
import com.example.common.infrastructure.security.principal.Role.WRITE
import com.example.common.infrastructure.security.principal.UserSession
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*

fun Application.configureSecurity() {
    install(Authentication) {
        form("auth-form") {
            userParamName = "username"
            passwordParamName = "password"
            validate { credentials ->
                if (credentials.name == "mnajborowski" && credentials.password == "maslo123") {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }

        session<UserSession>("auth-session-read") {
            validate { session: UserSession ->
                if (session.name == "mnajborowski") {
                    if (session.roles.contains(READ))
                        log.info("User roles verified: ${session.roles}")
                    else
                        return@validate null
                    log.info("User ${session.name} logged in by existing session")
                    session
                } else {
                    null
                }
            }
            challenge {
                call.respondText(status = HttpStatusCode.Unauthorized) { "You don't have permission to this resource." }
            }
        }

        session<UserSession>("auth-session-write") {
            validate { session: UserSession ->
                if (session.name == "mnajborowski") {
                    if (session.roles.contains(WRITE))
                        log.info("User roles verified: ${session.roles}")
                    else
                        return@validate null
                    log.info("User ${session.name} logged in by existing session")
                    session
                } else {
                    null
                }
            }
            challenge {
                call.respondText(status = HttpStatusCode.Unauthorized) { "You don't have permission to this resource." }
            }
        }
    }
}