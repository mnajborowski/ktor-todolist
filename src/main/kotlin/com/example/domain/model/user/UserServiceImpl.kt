package com.example.domain.model.user

import com.example.domain.model.user.dto.UserDTO
import com.example.domain.model.user.dto.updateWith
import org.jetbrains.exposed.sql.transactions.transaction

class UserServiceImpl : UserService {
    override fun getById(id: Int): User = transaction { User[id] }
    override fun findById(id: Int): User? = transaction { User.findById(id) }
    override fun create(userDTO: UserDTO): User = transaction {
        User.new {
            nickname = userDTO.nickname
            email = userDTO.email
        }
    }

    override fun update(userDTO: UserDTO): User = transaction {
        getById(userDTO.id!!).apply { updateWith(userDTO) }
    }

    override fun delete(id: Int) = transaction {
        getById(id).delete()
    }
}