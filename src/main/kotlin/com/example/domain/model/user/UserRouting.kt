package com.example.domain.model.user

import com.example.domain.model.user.dto.UserDTO
import com.example.domain.model.user.dto.toUserDTO
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
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
    get<UserParameters> { userParameters ->
        service.getById(userParameters.id).let {
            call.respond(it.toUserDTO())
        }
    }
}

fun Route.createUser(service: UserService) {
    post {
        call.receive<UserDTO>()
            .let(service::create)
            .let { call.respond(it.toUserDTO()) }
    }
}

fun Route.updateUser(service: UserService) {
    put {
        call.receive<UserDTO>()
            .let(service::update)
            .let { call.respond(it.toUserDTO()) }
    }
}

@KtorExperimentalLocationsAPI
fun Route.deleteUser(service: UserService) {
    delete<UserParameters> { userParameters ->
        service.delete(userParameters.id)
            .let { call.respond(HttpStatusCode.NoContent) }
    }
}

@KtorExperimentalLocationsAPI
@Location("/{id}")
data class UserParameters(val id: UserId)