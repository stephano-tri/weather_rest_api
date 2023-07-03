package eom.tri.weather.model.GovernmentAPI

import com.fasterxml.jackson.annotation.JsonProperty

data class ItemResult(
    @field:JsonProperty("item") val item: List<ShortTermForecast>
)
