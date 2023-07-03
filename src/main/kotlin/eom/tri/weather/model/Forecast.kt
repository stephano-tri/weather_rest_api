package eom.tri.weather.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Forecast(
    @field:JsonProperty("region_name") val regionName: String,
    @field:JsonProperty("region_code") val regionCode: String,
    @field:JsonProperty("baseDate") val baseDate: String,
    @field:JsonProperty("baseTime") val baseTime: String,
    @field:JsonProperty("fcstDate") val fcstDate: String,
    @field:JsonProperty("fcstByTime") val fcstByTime: List<ShortForecast>,
)
