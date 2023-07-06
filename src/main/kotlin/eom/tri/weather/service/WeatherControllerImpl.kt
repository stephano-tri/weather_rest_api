package eom.tri.weather.service

import eom.tri.weather.controller.WeatherController
import eom.tri.weather.exception.InvalidInputException
import eom.tri.weather.model.Address
import eom.tri.weather.model.GovernmentAPI.GovernmentPublicAPIMidResponse
import eom.tri.weather.model.GovernmentAPI.GovernmentPublicAPIResponse
import eom.tri.weather.model.GovernmentAPI.typed.MidTermForecast
import eom.tri.weather.model.GovernmentAPI.typed.MidTermTmpForecast
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
        val fixedTime = "0600"

        val searchMidtermForecast = midTermForecastRepository.findAllByRegIdAndRefDate(regionCode, refDate = today + fixedTime)
            .flatMap { midTermForecastEn ->
                MidForecast(
                    regId = midTermForecastEn.regId!!,
                    rnSt3Am = midTermForecastEn.rnSt3Am!!,
                    rnSt3Pm = midTermForecastEn.rnSt3Pm!!,
                    rnSt4Am = midTermForecastEn.rnSt4Am!!,
                    rnSt4Pm = midTermForecastEn.rnSt4Pm!!,
                    rnSt5Am = midTermForecastEn.rnSt5Am!!,
                    rnSt5Pm = midTermForecastEn.rnSt5Pm!!,
                    rnSt6Am = midTermForecastEn.rnSt6Am!!,
                    rnSt6Pm = midTermForecastEn.rnSt6Pm!!,
                    rnSt7Am = midTermForecastEn.rnSt7Am!!,
                    rnSt7Pm = midTermForecastEn.rnSt7Pm!!,
                    rnSt8 = midTermForecastEn.rnSt8!!,
                    rnSt9 = midTermForecastEn.rnSt9!!,
                    rnSt10 = midTermForecastEn.rnSt10!!,
                    wf3Am = midTermForecastEn.wf3Am!!,
                    wf3Pm = midTermForecastEn.wf3Pm!!,
                    wf4Am = midTermForecastEn.wf4Am!!,
                    wf4Pm = midTermForecastEn.wf4Pm!!,
                    wf5Am = midTermForecastEn.wf5Am!!,
                    wf5Pm = midTermForecastEn.wf5Pm!!,
                    wf6Am = midTermForecastEn.wf6Am!!,
                    wf6Pm = midTermForecastEn.wf6Pm!!,
                    wf7Am = midTermForecastEn.wf7Am!!,
                    wf7Pm = midTermForecastEn.wf7Pm!!,
                    wf8 = midTermForecastEn.wf8!!,
                    wf9 = midTermForecastEn.wf9!!,
                    wf10 = midTermForecastEn.wf10!!,
                    refDate = midTermForecastEn.refDate!!,
                    id = midTermForecastEn.id
                ).toMono()
            }

        return searchMidtermForecast.elementAt(0)
    }

    override fun getMidTermForecastTemp(regionCode : String) : Mono<MidTemperatureForecast> {
        val today = utilFunctions.toDateStr(LocalDateTime.now(), "yyyyMMdd")
        val fixedTime = "0600"

        val searchMidTermTemperatureForecast = midTermForecastTempRepository.findAllByRegIdAndRefDate(regionCode, refDate = today + fixedTime)
            .flatMap { midTermForecastTempEn ->
                MidTemperatureForecast(
                    id = midTermForecastTempEn.id,
                    regId = midTermForecastTempEn.regId!!,
                    refDate = midTermForecastTempEn.refDate!!,
                    taMin3 = midTermForecastTempEn.taMin_3,
                    taMin3Low = midTermForecastTempEn.taMin_3Low,
                    taMin3High = midTermForecastTempEn.taMax_3High,
                    taMax3 = midTermForecastTempEn.taMax_3,
                    taMax3Low = midTermForecastTempEn.taMax_3Low,
                    taMax3High = midTermForecastTempEn.taMax_3High,
                    taMin4 = midTermForecastTempEn.taMin_4,
                    taMin4Low = midTermForecastTempEn.taMin_4Low,
                    taMin4High = midTermForecastTempEn.taMin_4High,
                    taMax4 = midTermForecastTempEn.taMax_4,
                    taMax4Low = midTermForecastTempEn.taMax_4Low,
                    taMax4High = midTermForecastTempEn.taMax_4High,
                    taMin5 = midTermForecastTempEn.taMin_5,
                    taMin5Low = midTermForecastTempEn.taMin_5Low,
                    taMin5High = midTermForecastTempEn.taMin_5High,
                    taMax5 = midTermForecastTempEn.taMax_5,
                    taMax5Low = midTermForecastTempEn.taMax_5Low,
                    taMax5High = midTermForecastTempEn.taMax_5High,
                    taMin6 = midTermForecastTempEn.taMin_6,
                    taMin6Low = midTermForecastTempEn.taMin_6Low,
                    taMin6High = midTermForecastTempEn.taMin_6High,
                    taMax6 = midTermForecastTempEn.taMax_6,
                    taMax6Low = midTermForecastTempEn.taMax_6Low,
                    taMax6High = midTermForecastTempEn.taMax_6High,
                    taMin7 = midTermForecastTempEn.taMin_7,
                    taMin7Low = midTermForecastTempEn.taMin_7Low,
                    taMin7High = midTermForecastTempEn.taMin_7High,
                    taMax7 = midTermForecastTempEn.taMax_7,
                    taMax7Low = midTermForecastTempEn.taMax_7Low,
                    taMax7High = midTermForecastTempEn.taMax_7High,
                    taMin8 = midTermForecastTempEn.taMin_8,
                    taMin8Low = midTermForecastTempEn.taMin_8Low,
                    taMin8High = midTermForecastTempEn.taMin_8High,
                    taMax8 = midTermForecastTempEn.taMax_8,
                    taMax8Low = midTermForecastTempEn.taMax_8Low,
                    taMax8High = midTermForecastTempEn.taMax_8High,
                    taMin9 = midTermForecastTempEn.taMin_9,
                    taMin9Low = midTermForecastTempEn.taMin_9Low,
                    taMin9High = midTermForecastTempEn.taMin_9High,
                    taMax9 = midTermForecastTempEn.taMax_9,
                    taMax9Low = midTermForecastTempEn.taMax_9Low,
                    taMax9High = midTermForecastTempEn.taMax_9High,
                    taMin10 = midTermForecastTempEn.taMin_10,
                    taMin10Low = midTermForecastTempEn.taMin_10Low,
                    taMin10High = midTermForecastTempEn.taMin_10High,
                    taMax10 = midTermForecastTempEn.taMax_10,
                    taMax10Low = midTermForecastTempEn.taMax_10Low,
                    taMax10High = midTermForecastTempEn.taMax_10High,
                ).toMono()
            }

        return searchMidTermTemperatureForecast.elementAt(0)
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
