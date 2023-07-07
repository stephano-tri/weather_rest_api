package eom.tri.weather.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id

data class MidForecast(
    @Id
    val id: Long? = null,
    val regId: String? = null,
    @field:JsonProperty("3일_후_오전_강수_확률") val rnSt3Am: Int? = null,
    @field:JsonProperty("3일_후_오후_강수_확률") val rnSt3Pm: Int? = null,
    @field:JsonProperty("4일_후_오전_강수_확률") val rnSt4Am: Int? = null,
    @field:JsonProperty("4일_후_오전_강수_확률") val rnSt4Pm: Int? = null,
    @field:JsonProperty("5일_후_오전_강수_확률") val rnSt5Am: Int? = null,
    @field:JsonProperty("5일_후_오전_강수_확률") val rnSt5Pm: Int? = null,
    @field:JsonProperty("6일_후_오전_강수_확률") val rnSt6Am: Int? = null,
    @field:JsonProperty("6일_후_오전_강수_확률") val rnSt6Pm: Int? = null,
    @field:JsonProperty("7일_후_오전_강수_확률") val rnSt7Am: Int? = null,
    @field:JsonProperty("7일_후_오전_강수_확률") val rnSt7Pm: Int? = null,
    @field:JsonProperty("8일_후_오전_강수_확률") val rnSt8: Int? = null,
    @field:JsonProperty("9일_후_오전_강수_확률") val rnSt9: Int? = null,
    @field:JsonProperty("10일_후_오전_강수_확률") val rnSt10: Int? = null,
    @field:JsonProperty("3일_후_오전_날씨_예보") val wf3Am: String? = null,
    @field:JsonProperty("3일_후_오후_날씨_예보") val wf3Pm: String? = null,
    @field:JsonProperty("4일_후_오전_날씨_예보") val wf4Am: String? = null,
    @field:JsonProperty("4일_후_오후_날씨_예보") val wf4Pm: String? = null,
    @field:JsonProperty("5일_후_오전_날씨_예보") val wf5Am: String? = null,
    @field:JsonProperty("5일_후_오후_날씨_예보") val wf5Pm: String? = null,
    @field:JsonProperty("6일_후_오전_날씨_예보") val wf6Am: String? = null,
    @field:JsonProperty("6일_후_오후_날씨_예보") val wf6Pm: String? = null,
    @field:JsonProperty("7일_후_오전_날씨_예보") val wf7Am: String? = null,
    @field:JsonProperty("7일_후_오후_날씨_예보") val wf7Pm: String? = null,
    @field:JsonProperty("8일_후_날씨_예보") val wf8: String? = null,
    @field:JsonProperty("9일_후_날씨_예보") val wf9: String? = null,
    @field:JsonProperty("10일_후_날씨_예보") val wf10: String? = null,
    val refDate: String? = null
)
