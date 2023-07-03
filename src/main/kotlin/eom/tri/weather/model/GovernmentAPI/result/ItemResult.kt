package eom.tri.weather.model.GovernmentAPI.result

import com.fasterxml.jackson.annotation.JsonProperty
import eom.tri.weather.model.GovernmentAPI.typed.ShortTermForecast

data class ItemResult(
    @field:JsonProperty("item") val item: List<ShortTermForecast>
)
