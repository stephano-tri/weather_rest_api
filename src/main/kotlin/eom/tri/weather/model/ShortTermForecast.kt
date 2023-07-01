package eom.tri.weather.model

import com.fasterxml.jackson.annotation.JsonProperty

data class ShortTermForecast(
    val numOfRows: Int,
    val pageNo: Int,
    val totalCount: Int,
    val resultCode: String,
    val resultMsg: String,
    val dataType: String,
    val baseDate: String,
    val baseTime: String,
    @field:JsonProperty("fcstDate") val fcstDate: String,
    @field:JsonProperty("fcstTime") val fcstTime: String,
    @field:JsonProperty("category") val category: String,
    @field:JsonProperty("fcstValue") val fcstValue: Int,
    @field:JsonProperty("nx") val nx: Int,
    @field:JsonProperty("ny") val ny: Int
)
