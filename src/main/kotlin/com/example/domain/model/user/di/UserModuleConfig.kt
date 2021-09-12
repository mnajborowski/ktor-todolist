package com.example.domain.model.user.di

import com.example.domain.model.user.UserService
import com.example.domain.model.user.UserServiceImpl
import org.koin.dsl.module

val userModule = module {
    single<UserService> { UserServiceImpl() }
}