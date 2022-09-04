package com.example.common.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.common.infrastructure.security.authorization.DigestConfiguration
import com.example.common.infrastructure.security.authorization.UserInfo
import com.example.common.infrastructure.security.principal.Role.READ
import com.example.common.infrastructure.security.principal.Role.WRITE
import com.example.common.infrastructure.security.principal.UserSession
import com.example.user.domain.service.UserService
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.auth.ldap.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.html.currentTimeMillis
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {
    val userService: UserService by inject()
    val httpClient = HttpClient(CIO) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.BODY
        }
        install(JsonFeature) {
            serializer =
                KotlinxSerializer(kotlinx.serialization.json.Json {
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                })
        }
    }

    install(Authentication) {
        basic("auth-basic") {
            validate { credentials ->
                if (
                    credentials.name == "mnajborowski" &&
                    credentials.password == "test"
                ) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }

        basic("auth-basic-hashed") {
            validate { credentials ->
                val digestFunction =
                    DigestConfiguration.digestFunctionStatic
                val hashedUserTable = UserHashedTableAuth(
                    table = mapOf(
                        "mnajborowski" to digestFunction("test")
                    ),
                    digester = digestFunction
                )
                hashedUserTable.authenticate(credentials)
            }
        }

        basic("auth-basic-hashed-manual") {
            validate { credentials ->
                val user = userService.getUser(credentials.name)
                if (
                    credentials.name == user.username &&
                    DigestConfiguration
                        .sha256(credentials.password, user.salt)
                        .contentEquals(user.passwordHash)
                ) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }

        digest("auth-digest") {
            realm = "/"
            digestProvider { username, _ ->
                userService.getUser(username).passwordHash
            }
        }

        form("auth-form") {
            userParamName = "username"
            passwordParamName = "password"
            validate { credentials ->
                if (credentials.name == "mnajborowski"
                    && credentials.password == "test"
                ) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }

        basic("auth-ldap") {
            validate { credentials ->
                ldapAuthenticate(
                    credentials,
                    "ldap://localhost:10389",
                    "cn=${credentials.name},ou=Users,dc=example,dc=com"
                )
            }
        }

        jwt("auth-jwt") {
            realm = "/"
            verifier(
                JWT
                    .require(Algorithm.HMAC256("secret"))
                    .withAudience("http://0.0.0.0:8080/hello")
                    .withIssuer("http://0.0.0.0:8080/")
                    .build()
            )
            validate { credential ->
                val username =
                    credential.payload.getClaim("username").asString()
                if (userService.findUser(username) != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }

        oauth("auth-oauth-github") {
            urlProvider = { "http://localhost:8080/callback" }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "github",
                    authorizeUrl =
                    "https://github.com/login/oauth/authorize",
                    accessTokenUrl =
                    "https://github.com/login/oauth/access_token",
                    requestMethod = HttpMethod.Post,
                    clientId = System.getenv("GITHUB_CLIENT_ID"),
                    clientSecret =
                    System.getenv("GITHUB_CLIENT_SECRET"),
                    defaultScopes = listOf("user")
                )
            }
            client = httpClient
        }

        session<UserSession>("auth-session-oauth") {
            validate { session: UserSession ->
                if (session.name != "null" &&
                    currentTimeMillis() < session.expiration
                ) {
                    val userInfo: UserInfo =
                        httpClient.get("https://api.github.com/user") {
                            headers {
                                append(
                                    name = HttpHeaders.Authorization,
                                    value = "token ${session.name}"
                                )
                            }
                        }
                    log.info(
                        "User ${userInfo.login} " +
                                "logged in by existing session"
                    )
                    session
                } else {
                    null
                }
            }
            challenge {
                call.respondRedirect("/login-oauth")
            }
        }

        session<UserSession>("auth-session") {
            validate { session: UserSession ->
                if (session.name != "null"
                    && currentTimeMillis() < session.expiration
                ) {
                    log.info(
                        "User ${session.name} " +
                                "logged in by existing session"
                    )
                    session
                } else {
                    null
                }
            }
            challenge {
                call.respondRedirect("/login")
            }
        }

        session<UserSession>("auth-session-read") {
            validate { session: UserSession ->
                if (session.name != "null"
                    && currentTimeMillis() < session.expiration
                ) {
                    if (session.roles.contains(READ))
                        log.info("User roles verified: ${session.roles}")
                    else
                        return@validate null
                    log.info(
                        "User ${session.name} " +
                                "logged in by existing session"
                    )
                    session
                } else {
                    null
                }
            }
            challenge {
                call.respondRedirect("/login")
            }
        }

        session<UserSession>("auth-session-write") {
            validate { session: UserSession ->
                if (session.name != "null"
                    && currentTimeMillis() < session.expiration
                ) {
                    if (session.roles.contains(WRITE))
                        log.info("User roles verified: ${session.roles}")
                    else
                        return@validate null
                    log.info(
                        "User ${session.name} " +
                                "logged in by existing session"
                    )
                    session
                } else {
                    null
                }
            }
            challenge {
                call.respondRedirect("/login")
            }
        }
    }
}