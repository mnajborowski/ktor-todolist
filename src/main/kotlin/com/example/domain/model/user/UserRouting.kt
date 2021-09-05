package com.example.domain.model.user

import com.example.domain.model.user.dto.UserDTO
import com.example.domain.model.user.dto.toUserDTO
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureUserRouting() {
    val service = UserServiceImpl()
    routing {
        route("/user") {
            getUserById(service)
            createUser(service)
        }
    }
}

@OptIn(KtorExperimentalLocationsAPI::class)
fun Route.getUserById(service: UserServiceImpl) {
    get<UserParameters> { userParameters ->
        service.getById(userParameters.id).let {
            call.respond(it.toUserDTO())
        }
    }
}

fun Route.createUser(service: UserServiceImpl) {
    post {
        call.receive<UserDTO>()
            .let(service::create)
            .let { call.respond(it.toUserDTO()) }
    }
}

@OptIn(KtorExperimentalLocationsAPI::class)
@Location("/{id}")
data class UserParameters(val id: UserId)