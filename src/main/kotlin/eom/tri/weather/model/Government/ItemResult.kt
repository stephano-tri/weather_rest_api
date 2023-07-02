package eom.tri.weather.model.Government

import com.fasterxml.jackson.annotation.JsonProperty
import eom.tri.weather.model.ShortTermForecast

data class ItemResult(
    @field:JsonProperty("item") val item: List<ShortTermForecast>
)
