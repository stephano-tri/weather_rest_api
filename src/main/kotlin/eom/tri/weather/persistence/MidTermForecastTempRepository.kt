package eom.tri.weather.persistence

import org.springframework.data.r2dbc.repository.R2dbcRepository

interface MidTermForecastTempRepository: R2dbcRepository<MidTermForecastTempEntity, Long> {
}
