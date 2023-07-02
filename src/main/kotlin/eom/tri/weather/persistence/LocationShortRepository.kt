package eom.tri.weather.persistence

import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux

interface LocationShortRepository: R2dbcRepository<LocationShortEntity, Long> {

    fun findAllByFirstLocLike(firstLoc : String): Flux<LocationShortEntity>
}
