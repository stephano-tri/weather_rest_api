package eom.tri.weather.model.Government

import com.fasterxml.jackson.annotation.JsonProperty
import eom.tri.weather.annotation.NoArgsConstructor

@NoArgsConstructor
data class GovernmentPublicAPIResponse(
    @field:JsonProperty("response") val response: HeaderAndBody
)
