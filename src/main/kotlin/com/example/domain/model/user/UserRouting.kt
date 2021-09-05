package com.example.domain.model.user

import com.example.domain.model.user.dto.UserDTO
import com.example.domain.model.user.dto.toUserDTO
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureUserRouting() {
    val service = UserService()
    routing {
        route("/user") {
            getUserById(service)
            createUser(service)
        }
    }
}

fun Route.getUserById(service: UserService) {
    get("/{id}") {
        service.getById(call.parameters["id"]?.toInt()).let {
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