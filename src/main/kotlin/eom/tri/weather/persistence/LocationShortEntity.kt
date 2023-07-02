package eom.tri.weather.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("location_short")
data class LocationShortEntity(
    @Id
    val id: Long? = null,
    val country: String,
    val adminDivCode: String,
    val firstLoc: String,
    val secondLoc: String? = null,
    val thirdLoc: String? = null,
    val posX: Int,
    val posY: Int,
    val longitudeFirst: Int,
    val longitudeSecond: Int,
    val longitudeThird: Double,
    val altitudeFirst: Int,
    val altitudeSecond: Int,
    val altitudeThird: Double,
    val longitudePerSec: Double,
    val altitudePerSec: Double,
    val locUpdate: Int? = null,
    val etc: String? = null,
    val etc1: String? = null,
    val etc2: String? = null
)
