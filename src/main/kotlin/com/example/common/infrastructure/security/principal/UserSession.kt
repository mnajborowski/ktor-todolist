package com.example.common.infrastructure.security.principal

import io.ktor.auth.*

data class UserSession(
    val name: String,
    val expiration: Long,
    val roles: Set<Role> = emptySet()
) : Principal
