package eom.tri.weather.service

import eom.tri.weather.controller.WeatherController
import eom.tri.weather.exception.InvalidInputException
import eom.tri.weather.model.Address
import eom.tri.weather.model.GovernmentAPI.GovernmentPublicAPIMidResponse
import eom.tri.weather.model.GovernmentAPI.GovernmentPublicAPIResponse
import eom.tri.weather.model.MidForecast
import eom.tri.weather.model.MidTemperatureForecast
import eom.tri.weather.model.ShortForecast
import eom.tri.weather.persistence.*
import eom.tri.weather.util.UtilFunction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.time.LocalDateTime

@RestController
class WeatherControllerImpl(
    private val locationShortRepository : LocationShortRepository,
    private val locationMidRepository : LocationMidRepository,
    private val locationMidTempRepository : LocationMidTempRepository,
    private val shortTermForecastRepository : ShortTermForecastRepository,
    private val midTermForecastRepository : MidTermForecastRepository,
    private val midTermForecastTempRepository : MidTermForecastTempRepository,
    private val requestService : RequestService,
    private val utilFunctions : UtilFunction,
) : WeatherController {
    val logger: Logger= LoggerFactory.getLogger(WeatherControllerImpl::class.java)

    override fun getTodayForecast(regionCode : String) : Mono<List<ShortForecast>> {
        //option for today forecast
        val today = utilFunctions.toDateStr(LocalDateTime.now(), "yyyyMMdd")
        val fixedTime = "0200"

        // find pos_x , pos_y by regionCode(adminDivCode)
        val searchCoordinateByRegionCode = locationShortRepository.findAllByAdminDivCode(regionCode)
            .flatMap { loc ->
                Mono.zip(loc.posX.toMono() , loc.posY.toMono())
            }

        return searchCoordinateByRegionCode
            .flatMap {
                shortTermForecastRepository.findAllByFcstDateAndNxAndNy(today, it.t1, it.t2)
                    .flatMap { shortTermForecastEn ->
                        ShortForecast(
                            fcstTime = shortTermForecastEn.fcstTime!!,
                            category = shortTermForecastEn.category!!,
                            fcstValue = shortTermForecastEn.fcstValue!!
                        ).toMono()
                    }.collectList()
            }
    }

    override fun getMidTermForecast(regionCode : String) : Mono<MidForecast> {
        val today = utilFunctions.toDateStr(LocalDateTime.now(), "yyyyMMdd")

        val searchMidtermForecast = midTermForecastRepository.findAllByRegIdAndRefDate(regionCode, refDate = today)

        TODO("Not yet implemented")
    }

    override fun getMidTermForecastTemp(regionCode : String) : Mono<MidTemperatureForecast> {
        val today = utilFunctions.toDateStr(LocalDateTime.now(), "yyyyMMdd")

        val searchMidTermTemperatureForecast = midTermForecastTempRepository.findAllByRegIdAndRefDate(regionCode, refDate = today)

        TODO("Not yet implemented")
    }

    override fun searchAddress(type: String, regionName : String) : Mono<List<Address>> {
        return if(type == "short") {
            locationShortRepository.findAllByFirstLocLike("%${regionName}%")
                .flatMap { loc ->
                    Address(
                        regionName=addressAppender(loc),
                        regionCode=loc.adminDivCode!!,
                        nx=loc.posX,
                        ny=loc.posY
                    ).toMono()
                }
                .collectList()
        } else if(type == "mid"){
            locationMidRepository.findAllByNameLike("%${regionName}%")
                .flatMap { loc ->
                    Address(
                        regionName=loc.name,
                        regionCode=loc.code,
                    ).toMono()
                }
                .collectList()
        } else if(type == "mid_temp") {
            locationMidTempRepository.findAllByNameLike("%${regionName}")
                .flatMap { loc ->
                    Address(
                        regionName = loc.name,
                        regionCode = loc.code,
                    ).toMono()
                }
                .collectList()
        } else {
            InvalidInputException("type is not valid (short , mid , mid_temp only)").toMono()
        }
    }

    private fun addressAppender(loc: LocationShortEntity) : String {
        return loc.firstLoc + (loc.secondLoc?.let { " " + it + (loc.thirdLoc?. let { " $it" } ?: run { "" })} ?: run { "" } )
    }

}
