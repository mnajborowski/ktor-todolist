package com.example.user.domain.repository

import com.example.user.domain.model.User

interface UserRepository {
    fun getById(id: Int): User
    fun getByUsername(username: String): User
    fun findById(id: Int): User?
    fun findByUsername(username: String): User?
    fun create(user: User): User
    fun update(user: User): User
    fun deleteById(id: Int)
}