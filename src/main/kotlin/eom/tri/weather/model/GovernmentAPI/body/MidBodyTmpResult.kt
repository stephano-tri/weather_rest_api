package eom.tri.weather.model.GovernmentAPI.body

import com.fasterxml.jackson.annotation.JsonProperty
import eom.tri.weather.model.GovernmentAPI.result.MidTmpItemResult

data class MidBodyTmpResult(
    @field:JsonProperty("dataType") val dataType: String,
    @field:JsonProperty("items") val items: MidTmpItemResult,
    @field:JsonProperty("pageNo") val pageNo: Int,
    @field:JsonProperty("numOfRows") val numOfRows: Int,
    @field:JsonProperty("totalCount") val totalCount: Int
)
