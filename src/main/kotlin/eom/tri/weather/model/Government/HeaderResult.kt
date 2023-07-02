package eom.tri.weather.model.Government

import com.fasterxml.jackson.annotation.JsonProperty

data class HeaderResult(
    @field:JsonProperty("resultCode") val resultCode: String,
    @field:JsonProperty("resultMsg") val resultMsg: String
)
