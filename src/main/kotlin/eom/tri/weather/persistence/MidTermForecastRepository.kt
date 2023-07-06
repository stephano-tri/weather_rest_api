package eom.tri.weather.persistence

import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface MidTermForecastRepository : R2dbcRepository<MidTermForecastEntity, Long> {
    fun findAllByRegIdAndRefDate(regId: String, refDate: String): Flux<MidTermForecastEntity>
}
