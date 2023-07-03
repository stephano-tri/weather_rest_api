package eom.tri.weather.model.GovernmentAPI.body

import com.fasterxml.jackson.annotation.JsonProperty
import eom.tri.weather.model.GovernmentAPI.result.MidItemResult

data class MidBodyResult(
    @field:JsonProperty("dataType") val dataType: String,
    @field:JsonProperty("items") val items: MidItemResult,
    @field:JsonProperty("pageNo") val pageNo: Int,
    @field:JsonProperty("numOfRows") val numOfRows: Int,
    @field:JsonProperty("totalCount") val totalCount: Int
)
