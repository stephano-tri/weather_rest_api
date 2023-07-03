package eom.tri.weather.persistence

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface LocationShortRepository: R2dbcRepository<LocationShortEntity, Long> {

    fun findAllByFirstLocLike(firstLoc : String): Flux<LocationShortEntity>

    fun findAllByAdminDivCode(adminDivCode: String): Mono<LocationShortEntity>

    @Query("SELECT DISTINCT ls.pos_x, ls.pos_y FROM location_short ls")
    fun findAllDistinctPos(): Flux<LocationShortEntity>
}


