package com.example.domain.model.user

import com.example.domain.model.user.dto.UserDTO
import com.example.domain.model.user.dto.updateWith
import org.jetbrains.exposed.sql.transactions.transaction

class UserService {
    fun getById(id: Int?): User = transaction { User[id ?: throw Exception("User id cannot be null.")] }
    fun findById(id: Int): User? = transaction { User.findById(id) }
    fun create(userDTO: UserDTO): User = transaction {
        User.new {
            nickname = userDTO.nickname
            email = userDTO.email
        }
    }
    fun update(userDTO: UserDTO) = transaction {
        getById(userDTO.id!!).updateWith(userDTO)
    }
    fun delete(id: Int) = transaction {
        getById(id).delete()
    }
}