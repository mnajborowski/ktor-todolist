package com.example.domain.model.user

import com.example.domain.model.user.dto.UserDTO
import com.example.domain.model.user.dto.updateWith
import org.jetbrains.exposed.dao.id.EntityID

class UserServiceMock : UserService {
    private val users = mutableMapOf<UserId, User>()
    private var idSequence = 1

    override fun getById(id: Int): User = users[id] ?: throw RuntimeException("User with id=$id not found.")

    override fun findById(id: Int): User? = users[id]

    override fun create(userDTO: UserDTO): User = User(EntityID(idSequence, Users))
        .apply { updateWith(userDTO) }
        .also {
            idSequence++
            users[it.id.value] = it
        }

    override fun update(userDTO: UserDTO) {
        users[userDTO.id!!] = getById(userDTO.id).apply { updateWith(userDTO) }
    }

    override fun delete(id: Int) {
        users.remove(id)
    }
}