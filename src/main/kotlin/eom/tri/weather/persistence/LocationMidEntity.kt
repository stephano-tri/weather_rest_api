package eom.tri.weather.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("location_mid")
data class LocationMidEntity(
    @Id
    val id: Long? = null,
    val name: String,
    val code: String,
)
