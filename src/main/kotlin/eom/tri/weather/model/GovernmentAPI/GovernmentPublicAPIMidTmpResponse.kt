package eom.tri.weather.model.GovernmentAPI

import com.fasterxml.jackson.annotation.JsonProperty
import eom.tri.weather.model.GovernmentAPI.body.HeaderAndMidTmpBody

data class GovernmentPublicAPIMidTmpResponse(
    @field:JsonProperty("response") val response: HeaderAndMidTmpBody
)
