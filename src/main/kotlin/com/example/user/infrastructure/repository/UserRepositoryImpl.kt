package com.example.user.infrastructure.repository

import com.example.user.domain.model.User
import com.example.user.domain.repository.UserRepository
import com.example.user.infrastructure.entity.CityEntity
import com.example.user.infrastructure.entity.UserEntity
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepositoryImpl : UserRepository {
    override fun getById(id: Int): User = transaction { UserEntity[id].toDomain() }
    override fun findById(id: Int): User? = transaction { UserEntity.findById(id)?.toDomain() }
    override fun create(user: User): User = transaction {
        UserEntity.new {
            nickname = user.nickname
            email = user.email
            city = CityEntity[user.city.id]
        }.toDomain()
    }

    override fun update(user: User): User = transaction {
        UserEntity[user.id!!].apply {
            nickname = user.nickname
            email = user.email
            city = CityEntity[user.city.id]
        }.toDomain()
    }

    override fun deleteById(id: Int) = transaction {
        UserEntity[id].delete()
    }
}