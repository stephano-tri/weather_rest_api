package eom.tri.weather.model.GovernmentAPI.body

import com.fasterxml.jackson.annotation.JsonProperty
import eom.tri.weather.model.GovernmentAPI.body.BodyResult
import eom.tri.weather.model.GovernmentAPI.result.HeaderResult

data class HeaderAndBody(
    @field:JsonProperty("header") val header: HeaderResult,
    @field:JsonProperty("body") val body: BodyResult,
)
