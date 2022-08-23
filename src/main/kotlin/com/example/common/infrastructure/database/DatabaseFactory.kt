package com.example.common.infrastructure.database

import com.example.common.infrastructure.security.authorization.DigestConfiguration
import com.example.user.infrastructure.entity.Cities
import com.example.user.infrastructure.entity.CityEntity
import com.example.user.infrastructure.entity.UserEntity
import com.example.user.infrastructure.entity.Users
import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory.getLogger
import kotlin.random.Random

object DatabaseFactory {
    private val log = getLogger(javaClass)

    fun connect(): Database =
        Database.connect("jdbc:postgresql://localhost/postgres", user = "michal.najborowski").also {
            log.info("Connecting to database")
        }

    fun init() {
        transaction {
            log.info("Initialising database")
            SchemaUtils.createMissingTablesAndColumns(Users, Cities)

            val cityId = Cities.insertAndGetId {
                it[name] = "Poznań"
            }

            val salt = Random.nextBytes(32)
            Users.insert {
                it[username] = "mnajborowski"
                it[email] = "mnajborowski@test.pl"
                it[city] = cityId
                it[passwordHash] = DigestConfiguration.md5("mnajborowski:/:test")
                it[this.salt] = salt
            }

//            Users.update({ Users.id eq 1 }) { it[username] = "michal.najborowski" }

            (Users innerJoin Cities)
                .slice(Users.username, Cities.name)
                .select { Users.username like "michal%" }
                .forEach {
                    println("${it[Users.username]} lives in ${it[Cities.name]}")
                }

            val wroclaw = CityEntity.new {
                name = "Wrocław"
            }

            UserEntity.new {
                username = "michal.najborowski"
                email = "michal.najborowski@test.pl"
                city = wroclaw
                passwordHash = DigestConfiguration.sha256("test", salt)
                this.salt = salt
            }

            UserEntity[2].city = CityEntity.find { Cities.name eq "Poznań" }.single()

            UserEntity[2].load(UserEntity::city)

//            Users.deleteWhere { Users.nickname like "michal%" }
//            UserEntity[2].delete()
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