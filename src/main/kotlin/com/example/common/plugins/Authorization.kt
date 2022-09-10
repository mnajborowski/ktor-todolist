package com.example.common.plugins

import com.example.common.infrastructure.security.principal.Role
import com.example.common.infrastructure.security.principal.UserSession
import com.example.common.infrastructure.security.principal.getConfigurationName
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import org.slf4j.LoggerFactory

class RoleBasedAuthorization {
    private val logger = LoggerFactory.getLogger(javaClass)

    class Configuration

    fun interceptPipeline(
        pipeline: ApplicationCallPipeline,
        configurationNames: List<String?> = listOf(null),
    ) {
        require(configurationNames.isNotEmpty()) {
            "At least one configuration name or default listOf(null)"
        }

        pipeline.insertPhaseAfter(
            ApplicationCallPipeline.Features,
            Authentication.ChallengePhase
        )
        pipeline.insertPhaseAfter(
            Authentication.ChallengePhase,
            AuthorizationPhase
        )

        pipeline.intercept(AuthorizationPhase) {
            val session = call.sessions.get<UserSession>()
                ?: throw SecurityException("Session not found")
            val roles = session.roles
            if (roles.none {
                    it.getConfigurationName() in configurationNames
                }
            ) {
                logger.warn(
                    "Authorization failed for ${call.request.path()}. " +
                            "User ${session.name} doesn't have any " +
                            "given roles."
                )
                throw SecurityException("Insufficient roles")
            }
        }
    }

    companion object Feature : ApplicationFeature<
            Application,
            Configuration,
            RoleBasedAuthorization
            > {
        val AuthorizationPhase: PipelinePhase =
            PipelinePhase("Authorize")

        override val key: AttributeKey<RoleBasedAuthorization> =
            AttributeKey("Authorization")

        override fun install(
            pipeline: Application,
            configure: Configuration.() -> Unit
        ): RoleBasedAuthorization {
            return RoleBasedAuthorization()
        }
    }
}

fun Route.requireRole(
    vararg roles: Role,
    build: Route.() -> Unit
): Route {
    require(roles.isNotEmpty()) {
        "At least one role need to be provided"
    }
    val configurationNames =
        roles.distinct().map { it.getConfigurationName() }
    val authorizedRoute = createChild(
        AuthorizationRouteSelector(configurationNames)
    )

    application.feature(RoleBasedAuthorization)
        .interceptPipeline(authorizedRoute, configurationNames)
    authorizedRoute.build()
    return authorizedRoute
}

class AuthorizationRouteSelector(private val names: List<String?>) :
    RouteSelector() {
    override fun evaluate(
        context: RoutingResolveContext,
        segmentIndex: Int
    ): RouteSelectorEvaluation {
        return RouteSelectorEvaluation(
            true,
            RouteSelectorEvaluation.qualityTransparent
        )
    }

    override fun toString(): String =
        "(authorize ${names.joinToString { it ?: "\"default\"" }})"
}