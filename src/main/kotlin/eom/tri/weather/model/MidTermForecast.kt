package eom.tri.weather.model

import com.fasterxml.jackson.annotation.JsonProperty

data class MidTermForecast (
    @field:JsonProperty("rnSt3Am") val rnSt3Am: Int,
    @field:JsonProperty("rnSt3Pm") val rnSt3Pm: Int,
    @field:JsonProperty("rnSt4Am") val rnSt4Am: Int,
    @field:JsonProperty("rnSt4Pm") val rnSt4Pm: Int,
    @field:JsonProperty("rnSt5Am") val rnSt5Am: Int,
    @field:JsonProperty("rnSt5Pm") val rnSt5Pm: Int,
    @field:JsonProperty("rnSt6Am") val rnSt6Am: Int,
    @field:JsonProperty("rnSt6Pm") val rnSt6Pm: Int,
    @field:JsonProperty("rnSt7Am") val rnSt7Am: Int,
    @field:JsonProperty("rnSt7Pm") val rnSt7Pm: Int,
    @field:JsonProperty("rnSt8") val rnSt8: Int,
    @field:JsonProperty("rnSt9") val rnSt9: Int,
    @field:JsonProperty("rnSt10") val rnSt10: Int,
    @field:JsonProperty("wf3Am") val wf3Am: String,
    @field:JsonProperty("wf3Pm") val wf3Pm: String,
    @field:JsonProperty("wf4Am") val wf4Am: String,
    @field:JsonProperty("wf4Pm") val wf4Pm: String,
    @field:JsonProperty("wf5Am") val wf5Am: String,
    @field:JsonProperty("wf5Pm") val wf5Pm: String,
    @field:JsonProperty("wf6Am") val wf6Am: String,
    @field:JsonProperty("wf6Pm") val wf6Pm: String,
    @field:JsonProperty("wf7Am") val wf7Am: String,
    @field:JsonProperty("wf7Pm") val wf7Pm: String,
    @field:JsonProperty("wf8") val wf8: String,
    @field:JsonProperty("wf9") val wf9: String,
    @field:JsonProperty("wf10") val wf10: String,
)
