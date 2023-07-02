package eom.tri.weather.service

import eom.tri.weather.controller.WeatherController
import eom.tri.weather.model.Address
import eom.tri.weather.model.Government.GovernmentPublicAPIResponse
import eom.tri.weather.model.ShortTermForecast
import eom.tri.weather.persistence.LocationMidRepository
import eom.tri.weather.persistence.LocationShortEntity
import eom.tri.weather.persistence.LocationShortRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@RestController
class WeatherControllerImpl(
    private val locationShortRepository : LocationShortRepository,
    private val locationMidRepository : LocationMidRepository,
    private val requestService : RequestService
) : WeatherController {
    val logger: Logger= LoggerFactory.getLogger(WeatherControllerImpl::class.java)

    override fun getTodayForecast(regionCode : String) : Mono<GovernmentPublicAPIResponse> {
        return requestService.getShortTermWeatherForecast(baseDate = "20230702", baseTime = "0500", posX = 55 , posY = 123)
    }

    override fun searchAddress(type: String, regionName : String) : Mono<List<Address>> {
        return if(type == "short") {
            locationShortRepository.findAllByFirstLocLike("%${regionName}%")
                .flatMap { loc ->
                    Address(
                        regionName=addressAppender(loc),
                        regionCode=loc.adminDivCode,
                        nx=loc.posX,
                        ny=loc.posY
                    ).toMono()
                }
                .collectList()
        } else {
            locationMidRepository.findAllByNameLike("%${regionName}%")
                .flatMap { loc ->
                    Address(
                        regionName=loc.name,
                        regionCode=loc.code,
                    ).toMono()
                }
                .collectList()
        }
    }


    private fun addressAppender(loc: LocationShortEntity) : String {
        return loc.firstLoc + (loc.secondLoc?.let { " " + it + (loc.thirdLoc?. let { " $it" } ?: run { "" })} ?: run { "" } )
    }

}
