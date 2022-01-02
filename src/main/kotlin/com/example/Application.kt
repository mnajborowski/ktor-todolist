package com.example

import com.example.infrastructure.database.DatabaseFactory
import com.example.plugins.configureDependencyInjection
import com.example.plugins.configureLogging
import com.example.plugins.configureRouting
import com.example.plugins.configureSecurity
import com.example.plugins.configureSerialization
import com.example.plugins.configureSessions
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureDependencyInjection()
        configureSessions()
        configureSecurity()
        configureSerialization()
        configureRouting()
//        configureSockets()
        configureLogging()

        DatabaseFactory.connect()
        DatabaseFactory.dropAndInit()
    }.start(wait = true)
}
