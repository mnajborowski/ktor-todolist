package com.example.user.api

import com.example.common.UserId
import com.example.user.api.dto.request.UserRequest
import com.example.user.domain.service.UserService
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.locations.put
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

@KtorExperimentalLocationsAPI
fun Application.configureUserRouting() {
    val service: UserService by inject()
    routing {
        route("/user") {
            getUserById(service)
            createUser(service)
            updateUser(service)
            deleteUser(service)
        }
    }
}

@KtorExperimentalLocationsAPI
fun Route.getUserById(service: UserService) {
    authenticate("auth-session-read") {
        get<UserParameters> { userParameters ->
            service.getUser(userParameters.id).let {
                call.respond(it.toUserResponse())
            }
        }
    }
}

fun Route.createUser(service: UserService) {
    authenticate("auth-session-write") {
        post {
            call.receive<UserRequest>()
                .toCreateUserCommand()
                .let(service::createUser)
                .let { call.respond(it.toUserResponse()) }
        }
    }
}

@OptIn(KtorExperimentalLocationsAPI::class)
fun Route.updateUser(service: UserService) {
    authenticate("auth-session-write") {
        put<UserParameters> { userParameters ->
            call.receive<UserRequest>()
                .toUpdateUserCommand(userParameters.id)
                .let(service::updateUser)
                .let { call.respond(it.toUserResponse()) }
        }
    }
}

@KtorExperimentalLocationsAPI
fun Route.deleteUser(service: UserService) {
    authenticate("auth-session-write") {
        delete<UserParameters> { userParameters ->
            service.deleteUser(userParameters.id)
                .let { call.respond(HttpStatusCode.NoContent) }
        }
    }
}

@KtorExperimentalLocationsAPI
@Location("/{id}")
data class UserParameters(val id: UserId)