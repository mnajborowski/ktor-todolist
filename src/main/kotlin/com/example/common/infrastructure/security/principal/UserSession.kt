package com.example.common.infrastructure.security.principal

import io.ktor.auth.*

data class UserSession(
    val name: String,
    val roles: Set<Role> = emptySet()
) : Principal
