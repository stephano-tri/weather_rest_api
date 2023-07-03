package eom.tri.weather.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("location_short")
data class LocationShortEntity(
    @Id
    val id: Long? = null,
    val country: String? = null,
    val adminDivCode: String? = null,
    val firstLoc: String? = null,
    val secondLoc: String? = null,
    val thirdLoc: String? = null,
    val posX: Int? = null,
    val posY: Int? = null,
    val longitudeFirst: Int? = null,
    val longitudeSecond: Int? = null,
    val longitudeThird: Double? = null,
    val altitudeFirst: Int? = null,
    val altitudeSecond: Int? = null,
    val altitudeThird: Double? = null,
    val longitudePerSec: Double? = null,
    val altitudePerSec: Double? = null,
    val locUpdate: Int? = null,
    val etc: String? = null,
    val etc1: String? = null,
    val etc2: String? = null
)
