package com.example.user.domain.service

import com.example.common.UserId
import com.example.user.domain.command.CreateUser
import com.example.user.domain.command.UpdateUser
import com.example.user.domain.model.User

interface UserService {
    fun getUser(id: UserId): User
    fun getUser(username: String): User
    fun findUser(id: UserId): User?
    fun findUser(username: String): User?
    fun createUser(command: CreateUser): User
    fun updateUser(command: UpdateUser): User
    fun deleteUser(id: UserId)
}