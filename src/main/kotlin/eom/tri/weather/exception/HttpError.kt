package eom.tri.weather.exception

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.HttpStatus
import java.time.ZonedDateTime

data class HttpError(
    @get:JsonProperty("id") val id: String = "",
    @get:JsonProperty("path") val path: String,
    @get:JsonProperty("status") val status: HttpStatus,
    @get:JsonProperty("message") val message: String,
    @get:JsonProperty("timestamp") val timestamp: ZonedDateTime= ZonedDateTime.now()
) {
    //  empty constructor for ObjectMapper.readValue()
    constructor() : this(
        id = "",
        path = "",
        status = HttpStatus.INTERNAL_SERVER_ERROR,
        message = HttpStatus.INTERNAL_SERVER_ERROR.name,
        timestamp = ZonedDateTime.now()
    )
}
