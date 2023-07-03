package eom.tri.weather.model.GovernmentAPI

import com.fasterxml.jackson.annotation.JsonProperty
import eom.tri.weather.annotation.NoArgsConstructor
import eom.tri.weather.model.GovernmentAPI.body.HeaderAndBody

@NoArgsConstructor
data class GovernmentPublicAPIResponse(
    @field:JsonProperty("response") val response: HeaderAndBody
)
