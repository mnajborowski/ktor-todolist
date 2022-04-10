package com.example.user.infrastructure.service

import com.example.common.UserId
import com.example.user.domain.command.CreateUser
import com.example.user.domain.command.UpdateUser
import com.example.user.domain.model.User
import com.example.user.domain.repository.CityRepository
import com.example.user.domain.repository.UserRepository
import com.example.user.domain.service.UserService

class UserServiceImpl(
    private val userRepository: UserRepository,
    private val cityRepository: CityRepository,
) : UserService {
    override fun getUser(id: UserId): User = userRepository.getById(id)

    override fun findUser(id: UserId): User? = userRepository.findById(id)

    override fun createUser(command: CreateUser): User = User(
        nickname = command.nickname,
        email = command.email,
        city = cityRepository.getById(command.cityId)
    ).let { userRepository.create(it) }

    override fun updateUser(command: UpdateUser): User = User(
        id = command.id,
        nickname = command.nickname,
        email = command.email,
        city = cityRepository.getById(command.cityId)
    ).let { userRepository.update(it) }

    override fun deleteUser(id: UserId) {
        userRepository.deleteById(id)
    }
}