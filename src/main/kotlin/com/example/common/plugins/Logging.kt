package com.example.common.plugins

import io.ktor.application.*
import io.ktor.features.*

fun Application.configureLogging() {
    install(CallLogging)
}