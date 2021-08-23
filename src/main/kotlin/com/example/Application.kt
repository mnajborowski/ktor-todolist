package com.example

import com.example.domain.model.user.User
import com.example.domain.model.user.dto.UserDTO
import com.example.domain.model.user.dto.updateWith
import com.example.infrastructure.database.DatabaseFactory
import com.example.plugins.configureRouting
import com.example.plugins.configureSecurity
import com.example.plugins.configureSerialization
import com.example.plugins.configureSockets
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureSecurity()
        configureSerialization()
        configureSockets()

        DatabaseFactory.connect().let(DatabaseFactory::dropAndInit)
        transaction {
            val user1 = User[1]
            val user2 = UserDTO(user1).copy(age = 18, nickname = "Guy")
            user1.updateWith(user2)
        }
    }.start(wait = true)
}
