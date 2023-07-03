package eom.tri.weather.model

import com.fasterxml.jackson.annotation.JsonProperty

data class ShortForecast(
    @field:JsonProperty("fcstTime") val fcstTime: String,
    @field:JsonProperty("category") val category: String,
    @field:JsonProperty("fcstValue") val fcstValue: String
    )
