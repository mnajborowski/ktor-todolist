package com.example.infrastructure.database

import com.example.domain.model.user.User
import com.example.domain.model.user.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

object DatabaseFactory {
    private val log = LoggerFactory.getLogger(this::class.java)

    fun connect(): Database =
        log.info("Connecting to database").let {
            Database.connect("jdbc:postgresql://localhost/postgres", user = "michal.najborowski")
        }


    fun init() {
        transaction {
            log.info("Initialising database")
            SchemaUtils.createMissingTablesAndColumns(
                Users
            )

            User.new {
                age = 10
                nickname = "Jeff"
            }

            User.new {
                age = 18
                nickname = "Pacman"
            }
        }
    }

    fun dropAndInit(database: Database) {
        transaction {
            log.info("Dropping all tables")
            SchemaUtils.drop(Users)
        }
        init()
    }
}