package eom.tri.weather.model.GovernmentAPI

import com.fasterxml.jackson.annotation.JsonProperty

data class HeaderAndBody(
    @field:JsonProperty("header") val header: HeaderResult,
    @field:JsonProperty("body") val body: BodyResult,
)
