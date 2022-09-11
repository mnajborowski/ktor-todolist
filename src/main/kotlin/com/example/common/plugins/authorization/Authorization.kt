package com.example.common.plugins.authorization

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.routing.*
import io.ktor.util.*
import io.ktor.util.pipeline.*

class Authorization {
    fun interceptPipeline(
        pipeline: ApplicationCallPipeline,
        configurationNames: List<String?> = listOf(null),
        block: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit,
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

        pipeline.intercept(AuthorizationPhase, block)
    }

    companion object Feature : ApplicationFeature<
            Application,
            Any,
            Authorization
            > {
        val AuthorizationPhase: PipelinePhase =
            PipelinePhase("Authorize")

        override val key: AttributeKey<Authorization> =
            AttributeKey("Authorization")

        override fun install(
            pipeline: Application,
            configure: Any.() -> Unit
        ): Authorization {
            return Authorization()
        }
    }
}

fun Route.authorize(
    configurationNames: List<String?> = listOf(null),
    build: Route.() -> Unit,
    block: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit,
): Route {
    val authorizedRoute = createChild(
        AuthorizationRouteSelector(configurationNames)
    )
    application.feature(Authorization)
        .interceptPipeline(
            authorizedRoute,
            configurationNames,
            block
        )
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