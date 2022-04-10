package com.example

import com.example.common.infrastructure.database.DatabaseFactory
import com.example.common.plugins.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureDependencyInjection()
        configureSessions()
        configureSecurity()
        configureSerialization()
        configureRouting()
        configureLogging()

        DatabaseFactory.connect()
        DatabaseFactory.dropAndInit()
    }.start(wait = true)
}
