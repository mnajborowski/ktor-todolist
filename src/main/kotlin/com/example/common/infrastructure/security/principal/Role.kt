package com.example.common.infrastructure.security.principal

enum class Role {
    READ,
    WRITE
}

fun Role.getConfigurationName() =
    "auth-role-${this.toString().lowercase()}"