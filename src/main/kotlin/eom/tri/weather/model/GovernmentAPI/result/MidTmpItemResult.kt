package eom.tri.weather.model.GovernmentAPI.result

import com.fasterxml.jackson.annotation.JsonProperty
import eom.tri.weather.model.GovernmentAPI.typed.MidTermTmpForecast

data class MidTmpItemResult(
    @field:JsonProperty("item") val item: List<MidTermTmpForecast>
)
