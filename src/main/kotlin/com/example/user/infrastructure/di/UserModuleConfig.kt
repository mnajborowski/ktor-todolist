package com.example.user.infrastructure.di

import com.example.user.domain.repository.CityRepository
import com.example.user.domain.repository.UserRepository
import com.example.user.domain.service.UserService
import com.example.user.infrastructure.repository.CityRepositoryImpl
import com.example.user.infrastructure.repository.UserRepositoryImpl
import com.example.user.infrastructure.service.UserServiceImpl
import org.koin.dsl.module

val userModule = module {
    single<UserRepository> { UserRepositoryImpl() }
    single<CityRepository> { CityRepositoryImpl() }
    single<UserService> { UserServiceImpl(get(), get()) }
}