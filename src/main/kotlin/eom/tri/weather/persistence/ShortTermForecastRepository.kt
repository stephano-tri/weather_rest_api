package eom.tri.weather.persistence

import org.springframework.data.r2dbc.repository.R2dbcRepository

interface ShortTermForecastRepository: R2dbcRepository<ShortTermForecastEntity, Long> {

}
