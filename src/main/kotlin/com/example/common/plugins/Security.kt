package com.example.common.plugins

import com.example.common.infrastructure.security.authorization.DigestConfiguration
import com.example.common.infrastructure.security.principal.Role.READ
import com.example.common.infrastructure.security.principal.Role.WRITE
import com.example.common.infrastructure.security.principal.UserSession
import com.example.user.domain.service.UserService
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {
    val userService: UserService by inject()

    install(Authentication) {
        basic("auth-basic") {
            validate { credentials ->
                if (
                    credentials.name == "mnajborowski" &&
                    credentials.password == "maslo123"
                ) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }

        basic("auth-basic-hashed") {
            validate { credentials ->
                val digestFunction = DigestConfiguration.digestFunctionStatic
                val hashedUserTable = UserHashedTableAuth(
                    table = mapOf(
                        "mnajborowski" to digestFunction("maslo123")
                    ),
                    digester = digestFunction
                )
                hashedUserTable.authenticate(credentials)
            }
        }

        basic("auth-basic-hashed-manual") {
            validate { credentials ->
                val user = userService.getUser(credentials.name)
                if (
                    credentials.name == user.username &&
                    DigestConfiguration.sha256(credentials.password, user.salt).contentEquals(user.passwordHash)
                ) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }

        digest("auth-digest") {
            realm = "/"
            digestProvider { username, _ ->
                userService.getUser(username).passwordHash
            }
        }

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
                if (session.name == "mnajborowski" || session.name == "michal.najborowski") {
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