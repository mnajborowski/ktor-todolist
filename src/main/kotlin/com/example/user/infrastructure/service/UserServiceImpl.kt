package com.example.user.infrastructure.service

import com.example.common.UserId
import com.example.common.infrastructure.security.authorization.DigestConfiguration
import com.example.user.domain.command.CreateUser
import com.example.user.domain.command.UpdateUser
import com.example.user.domain.model.User
import com.example.user.domain.repository.CityRepository
import com.example.user.domain.repository.UserRepository
import com.example.user.domain.service.UserService
import kotlin.random.Random

class UserServiceImpl(
    private val userRepository: UserRepository,
    private val cityRepository: CityRepository,
) : UserService {
    override fun getUser(id: UserId): User = userRepository.getById(id)
    override fun getUser(username: String): User = userRepository.getByUsername(username)

    override fun findUser(id: UserId): User? = userRepository.findById(id)
    override fun findUser(username: String): User? = userRepository.findByUsername(username)

    override fun createUser(command: CreateUser): User {
        val salt = Random.nextBytes(32)
        return User(
            username = command.nickname,
            email = command.email,
            city = cityRepository.getById(command.cityId),
            passwordHash = DigestConfiguration.sha256(command.password, salt),
            salt = salt
        ).let { userRepository.create(it) }
    }

    override fun updateUser(command: UpdateUser): User = User(
        id = command.id,
        username = command.nickname,
        email = command.email,
        city = cityRepository.getById(command.cityId)
    ).let { userRepository.update(it) }

    override fun deleteUser(id: UserId) {
        userRepository.deleteById(id)
    }
}