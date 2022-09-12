package com.example

import com.example.common.infrastructure.database.DatabaseFactory
import com.example.common.plugins.configureDependencyInjection
import com.example.common.plugins.configureGraphQL
import com.example.common.plugins.configureLogging
import com.example.common.plugins.configureRouting
import com.example.common.plugins.configureSecurity
import com.example.common.plugins.configureSerialization
import com.example.common.plugins.configureSessions
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureDependencyInjection()
        configureSessions()
        configureSecurity()
        configureSerialization()
        configureRouting()
        configureGraphQL()
        configureLogging()

        DatabaseFactory.connect()
        DatabaseFactory.dropAndInit()
    }.start(wait = true)
}
