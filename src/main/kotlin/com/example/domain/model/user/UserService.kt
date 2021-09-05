package com.example.domain.model.user

import com.example.domain.model.user.dto.UserDTO

interface UserService {
    fun getById(id: Int): User
    fun findById(id: Int): User?
    fun create(userDTO: UserDTO): User
    fun update(userDTO: UserDTO)
    fun delete(id: Int)
}