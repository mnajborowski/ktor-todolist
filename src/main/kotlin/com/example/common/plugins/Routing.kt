package com.example.common.plugins

import com.example.common.infrastructure.security.authorization.configureAuthorizationRouting
import com.example.user.api.configureUserRouting
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException

@OptIn(KtorExperimentalLocationsAPI::class)
fun Application.configureRouting() {
    install(Locations)
    install(StatusPages) {
        exception<EntityNotFoundException> { cause ->
            call.respond(HttpStatusCode.NotFound, cause.localizedMessage)
        }
    }

    configureUserRouting()
    configureAuthorizationRouting()
}
