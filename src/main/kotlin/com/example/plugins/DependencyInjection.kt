package com.example.plugins

import com.example.domain.model.user.di.userModule
import io.ktor.application.*
import org.koin.ktor.ext.Koin
import org.koin.logger.SLF4JLogger

fun Application.configureDependencyInjection() {
    install(Koin) {
        SLF4JLogger()
        modules(userModule)
    }
}