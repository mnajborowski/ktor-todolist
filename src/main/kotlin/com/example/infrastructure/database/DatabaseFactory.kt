package com.example.infrastructure.database

import com.example.Cities
import com.example.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

object DatabaseFactory {
    private val log = LoggerFactory.getLogger(this::class.java)

    fun connect() {
        log.info("Connecting to database")
        Database.connect("jdbc:postgresql://localhost/", user = "michal.najborowski")
    }

    fun init() {
        transaction {
            log.info("Initialising database")
            SchemaUtils.createMissingTablesAndColumns(Cities, Users)

            val saintPetersburgId = Cities.insert {
                it[name] = "St. Petersburg"
            } get Cities.id

            val munichId = Cities.insert {
                it[name] = "Munich"
            } get Cities.id


            Users.insert {
                it[name] = "Andrey"
                it[cityId] = saintPetersburgId
            }

            Users.insert {
                it[name] = "Sergey"
                it[cityId] = munichId
            }
        }
    }

    fun dropAndInit() {
        transaction {
            log.info("Dropping all tables")
            SchemaUtils.drop(Users, Cities)
        }
        init()
    }
}