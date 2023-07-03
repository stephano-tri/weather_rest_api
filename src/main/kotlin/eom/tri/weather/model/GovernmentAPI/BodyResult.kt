package eom.tri.weather.model.GovernmentAPI

import com.fasterxml.jackson.annotation.JsonProperty

data class BodyResult(
    @field:JsonProperty("dataType") val dataType: String,
    @field:JsonProperty("items") val items: ItemResult,
    @field:JsonProperty("pageNo") val pageNo: Int,
    @field:JsonProperty("numOfRows") val numOfRows: Int,
    @field:JsonProperty("totalCount") val totalCount: Int
)
