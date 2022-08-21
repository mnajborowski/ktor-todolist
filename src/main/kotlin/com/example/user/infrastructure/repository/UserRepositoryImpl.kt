package com.example.user.infrastructure.repository

import com.example.user.domain.model.User
import com.example.user.domain.repository.UserRepository
import com.example.user.infrastructure.entity.CityEntity
import com.example.user.infrastructure.entity.UserEntity
import com.example.user.infrastructure.entity.Users
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepositoryImpl : UserRepository {
    override fun getById(id: Int): User = transaction { UserEntity[id].toDomain() }
    override fun getByUsername(username: String): User = transaction { UserEntity.find { Users.username eq username }.single().toDomain() }
    override fun findById(id: Int): User? = transaction { UserEntity.findById(id)?.toDomain() }
    override fun create(user: User): User = transaction {
        UserEntity.new {
            email = user.email
            city = CityEntity[user.city.id]

            username = user.username
            passwordHash = user.passwordHash
        }.toDomain()
    }

    override fun update(user: User): User = transaction {
        UserEntity[user.id!!].apply {
            username = user.username
            email = user.email
            city = CityEntity[user.city.id]
        }.toDomain()
    }

    override fun deleteById(id: Int) = transaction {
        UserEntity[id].delete()
    }
}