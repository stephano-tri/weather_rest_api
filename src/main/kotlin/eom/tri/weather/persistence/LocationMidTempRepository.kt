package eom.tri.weather.persistence

import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux

interface LocationMidTempRepository: R2dbcRepository<LocationMidTempEntity, Long> {
    fun findAllByNameLike(name: String): Flux<LocationMidTempEntity>
}
