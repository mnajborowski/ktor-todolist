package com.example.common.infrastructure.database

import com.example.user.infrastructure.entity.Cities
import com.example.user.infrastructure.entity.CityEntity
import com.example.user.infrastructure.entity.UserEntity
import com.example.user.infrastructure.entity.Users
import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory.getLogger

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

            Users.insert {
                it[nickname] = "mnajborowski"
                it[email] = "test@test.pl"
                it[city] = cityId
            }

            Users.update({ Users.id eq 1 }) { it[nickname] = "michal.najborowski" }

            (Users innerJoin Cities)
                .slice(Users.nickname, Cities.name)
                .select { Users.nickname like "michal%" }
                .forEach {
                    println("${it[Users.nickname]} lives in ${it[Cities.name]}")
                }

            val wroclaw = CityEntity.new {
                name = "Wrocław"
            }

            UserEntity.new {
                nickname = "michaelo.angelo"
                email = "michaelo.angelo@test.pl"
                city = wroclaw
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