package eom.tri.weather.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("short_term_forecast")
data class ShortTermForecastEntity(
    @Id val id: Long? = null,
    val baseDate: String? = null,
    val baseTime: String? = null,
    val fcstDate: String? = null,
    val fcstTime: String? = null,
    val nx: Int? = null,
    val ny: Int? = null,
    val fcstValue: String? = null,
    var category: String? = null,
)
