package eom.tri.weather.model.GovernmentAPI.result

import com.fasterxml.jackson.annotation.JsonProperty
import eom.tri.weather.model.GovernmentAPI.typed.MidTermForecast

data class MidItemResult(
    @field:JsonProperty("item") val item: List<MidTermForecast>
)
