package com.example.common.infrastructure.security.principal

enum class Role {
    READ,
    WRITE
}

fun Collection<Role>.getConfigurationNames() =
    distinct().map { "auth-role-${it.toString().lowercase()}" }