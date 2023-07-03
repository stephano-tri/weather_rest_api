package eom.tri.weather.persistence

import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Mono

interface ShortTermForecastRepository: R2dbcRepository<ShortTermForecastEntity, Long> {
    fun existsByFcstDateAndNxAndNy(fcstDate: String, nx: Int, ny: Int): Mono<Boolean>

}
