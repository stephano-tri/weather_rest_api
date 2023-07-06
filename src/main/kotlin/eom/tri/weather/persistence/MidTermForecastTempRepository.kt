package eom.tri.weather.persistence

import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux

interface MidTermForecastTempRepository: R2dbcRepository<MidTermForecastTempEntity, Long> {
    fun findAllByRegIdAndRefDate(regId: String, refDate: String): Flux<MidTermForecastTempEntity>
}
