package eom.tri.weather.model.GovernmentAPI

import com.fasterxml.jackson.annotation.JsonProperty

data class Address(
    @field:JsonProperty("region_name") val regionName: String,
    @field:JsonProperty("region_code") val regionCode: String,
    @field:JsonProperty("nx") val nx: Int? = 0,
    @field:JsonProperty("ny") val ny: Int? = 0,
)
