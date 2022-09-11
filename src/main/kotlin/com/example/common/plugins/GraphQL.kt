package com.example.common.plugins

import com.apurebase.kgraphql.GraphQL
import com.example.common.infrastructure.security.principal.Role.READ
import com.example.common.infrastructure.security.principal.Role.WRITE
import com.example.common.plugins.authorization.requireRole
import com.example.user.api.dto.request.UserRequest
import com.example.user.api.dto.response.CityResponse
import com.example.user.api.dto.response.UserResponse
import com.example.user.domain.service.UserService
import io.ktor.application.*
import io.ktor.auth.*
import org.koin.ktor.ext.inject

fun Application.configureGraphQL() {
    val service: UserService by inject()
    install(GraphQL) {
        playground = true
        schema {
            type<UserResponse> { description = "User object" }
            type<CityResponse> { description = "City object" }
            inputType<UserRequest> { description = "User input" }

            wrap { route ->
                authenticate("auth-session") {
                    requireRole(READ, WRITE, build = route)
                }
            }

            query("user") {
                description = "Returns a user with given username"
                resolver { username: String ->
                    service.getUser(
                        username
                    ).toUserResponse()
                }
            }

            mutation("addUser") {
                description = "Adds a new user"
                resolver { user: UserRequest ->
                    service.createUser(
                        user.toCreateUserCommand()
                    ).toUserResponse()
                }
            }
        }
    }
}