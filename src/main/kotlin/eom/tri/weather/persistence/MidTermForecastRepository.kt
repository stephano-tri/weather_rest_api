package eom.tri.weather.persistence

import org.springframework.data.r2dbc.repository.R2dbcRepository

interface MidTermForecastRepository : R2dbcRepository<MidTermForecastEntity, Long> {
}
