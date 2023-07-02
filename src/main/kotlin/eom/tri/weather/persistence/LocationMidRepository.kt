package eom.tri.weather.persistence

import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux

interface LocationMidRepository: R2dbcRepository<LocationMidEntity, Long> {

    fun findAllByNameLike(name: String): Flux<LocationMidEntity>

}
