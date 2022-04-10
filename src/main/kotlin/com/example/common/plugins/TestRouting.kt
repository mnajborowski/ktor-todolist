package com.example.common.plugins

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureTestRouting() {
    routing {
        get("/") {
            call.respondText { "Hello World!" }
        }
    }
}