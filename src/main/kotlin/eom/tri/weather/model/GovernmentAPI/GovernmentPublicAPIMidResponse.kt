package eom.tri.weather.model.GovernmentAPI

import com.fasterxml.jackson.annotation.JsonProperty
import eom.tri.weather.model.GovernmentAPI.body.HeaderAndBody
import eom.tri.weather.model.GovernmentAPI.body.HeaderAndMidBody

data class GovernmentPublicAPIMidResponse(
    @field:JsonProperty("response") val response: HeaderAndMidBody
)
