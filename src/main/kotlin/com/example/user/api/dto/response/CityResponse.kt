package com.example.user.api.dto.response

import com.example.common.CityId
import kotlinx.serialization.Serializable

@Serializable
data class CityResponse(
    val id: CityId,
    val name: String,
)
