package com.example.domain.model.user

import com.example.domain.model.user.dto.UserDTO
import com.example.domain.model.user.dto.toUserDTO
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.configureUserRouting() {
    val service: UserService by inject()
    routing {
        route("/user") {
            getUserById(service)
            createUser(service)
        }
    }
}

@OptIn(KtorExperimentalLocationsAPI::class)
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

@OptIn(KtorExperimentalLocationsAPI::class)
@Location("/{id}")
data class UserParameters(val id: UserId)