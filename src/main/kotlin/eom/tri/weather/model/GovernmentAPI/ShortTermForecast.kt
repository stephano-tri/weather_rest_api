package eom.tri.weather.model.GovernmentAPI

import com.fasterxml.jackson.annotation.JsonProperty

data class ShortTermForecast(
    @field:JsonProperty("baseDate") val baseDate: String,
    @field:JsonProperty("baseTime") val baseTime: String,
    @field:JsonProperty("fcstDate") val fcstDate: String,
    @field:JsonProperty("fcstTime") val fcstTime: String,
    @field:JsonProperty("category") val category: String,
    @field:JsonProperty("fcstValue") val fcstValue: String,
    @field:JsonProperty("nx") val nx: Int,
    @field:JsonProperty("ny") val ny: Int
)
