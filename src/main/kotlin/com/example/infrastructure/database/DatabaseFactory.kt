package com.example.infrastructure.database

import com.example.domain.model.user.UserService
import com.example.domain.model.user.Users
import com.example.domain.model.user.dto.UserDTO
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory.getLogger

object DatabaseFactory {
    private val log = getLogger(javaClass)

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

            val service = UserService()
            service.create(UserDTO(null, 18, "Michu"))
            service.update(UserDTO(1, 24, "Najbo"))
//            service.delete(1)
        }
    }

    fun dropAndInit() {
        transaction {
            log.info("Dropping all tables")
            SchemaUtils.drop(Users)
        }
        init()
    }
}