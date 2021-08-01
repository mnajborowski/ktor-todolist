package com.example

import com.example.infrastructure.database.DatabaseFactory
import com.example.plugins.configureRouting
import com.example.plugins.configureSecurity
import com.example.plugins.configureSerialization
import com.example.plugins.configureSockets
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.dao.id.IntIdTable

object Users : IntIdTable("user") {
    val name = varchar("name", length = 50) // Column<String>
    val cityId = reference("city_id", Cities)
}

object Cities : IntIdTable("city") {
    val name = varchar("name", 50) // Column<String>
}

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureSecurity()
        configureSerialization()
        configureSockets()

        DatabaseFactory.connect()
        DatabaseFactory.dropAndInit()
    }.start(wait = true)
}
