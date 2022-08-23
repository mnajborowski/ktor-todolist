package com.example.common.infrastructure.security.authorization

import io.ktor.util.*
import java.security.MessageDigest

object DigestConfiguration {
    val digestFunctionStatic =
        getDigestFunction("SHA-256") { "ktor${it.length}" }
    fun sha256(text: String, salt: ByteArray): ByteArray =
        with(MessageDigest.getInstance("SHA-256")) {
            update(salt)
            digest(text.toByteArray())
        }
    fun md5(text: String): ByteArray =
        MessageDigest.getInstance("MD5").digest(text.toByteArray())
}