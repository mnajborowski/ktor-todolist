package com.example.common.infrastructure.security.authorization

import io.ktor.util.*
import java.security.MessageDigest
import kotlin.random.Random

object DigestConfiguration {
    val digestFunctionStatic =
        getDigestFunction("SHA-256") { "ktor${it.length}" }
    val digestFunctionRandom =
        getDigestFunction("SHA-256") { Random.nextBytes(32).toString() }
    fun digestFunctionCustom(salt: ByteArray) =
        getDigestFunction("SHA-256") { salt.toString() }


    fun sha256(text: String, salt: ByteArray): ByteArray =
        with(MessageDigest.getInstance("SHA-256")) {
            update(salt)
            digest(text.toByteArray())
        }
    fun md5(text: String): ByteArray = MessageDigest.getInstance("MD5").digest(text.toByteArray())
}