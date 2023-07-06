package eom.tri.weather.service

import eom.tri.weather.controller.WeatherController
import eom.tri.weather.exception.InvalidInputException
import eom.tri.weather.model.Address
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

        // find pos_x , pos_y by regionCode(adminDivCode)
        val searchCoordinateByRegionCode = locationShortRepository.findAllByAdminDivCode(regionCode)
            .flatMap { loc ->
                Mono.zip(loc.posX.toMono() , loc.posY.toMono())
            }

        return searchCoordinateByRegionCode
            .flatMap {
                shortTermForecastRepository.findAllByFcstDateAndNxAndNy(today, it.t1, it.t2)
                    .flatMap { shortTermForecastEn ->
                        shortTermForecastEn.cast2ShortTermPojo()
                    }.collectList()
            }
    }

    override fun getMidTermForecast(regionCode : String) : Mono<MidForecast> {
        val today = utilFunctions.toDateStr(LocalDateTime.now(), "yyyyMMdd")
        val fixedTime = "0600"

        val searchMidtermForecast = midTermForecastRepository.findAllByRegIdAndRefDate(regionCode, refDate = today + fixedTime)
            .flatMap { midTermForecastEn ->
                midTermForecastEn.cast2MidForecastPojo()
            }

        return searchMidtermForecast.elementAt(0)
    }

    override fun getMidTermForecastTemp(regionCode : String) : Mono<MidTemperatureForecast> {
        val today = utilFunctions.toDateStr(LocalDateTime.now(), "yyyyMMdd")
        val fixedTime = "0600"

        val searchMidTermTemperatureForecast = midTermForecastTempRepository.findAllByRegIdAndRefDate(regionCode, refDate = today + fixedTime)
            .flatMap { midTermForecastTempEn ->
                midTermForecastTempEn.cast2MidForecastTemperaturePojo()
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

    private fun ShortTermForecastEntity.cast2ShortTermPojo(): Mono<ShortForecast> {
        return ShortForecast(
            fcstTime = this.fcstTime!!,
            category = this.category!!,
            fcstValue = this.fcstValue!!
        ).toMono()
    }

    private fun MidTermForecastEntity.cast2MidForecastPojo() : Mono<MidForecast> =
        MidForecast(
            regId = this.regId!!,
            rnSt3Am = this.rnSt3Am!!,
            rnSt3Pm = this.rnSt3Pm!!,
            rnSt4Am = this.rnSt4Am!!,
            rnSt4Pm = this.rnSt4Pm!!,
            rnSt5Am = this.rnSt5Am!!,
            rnSt5Pm = this.rnSt5Pm!!,
            rnSt6Am = this.rnSt6Am!!,
            rnSt6Pm = this.rnSt6Pm!!,
            rnSt7Am = this.rnSt7Am!!,
            rnSt7Pm = this.rnSt7Pm!!,
            rnSt8 = this.rnSt8!!,
            rnSt9 = this.rnSt9!!,
            rnSt10 = this.rnSt10!!,
            wf3Am = this.wf3Am!!,
            wf3Pm = this.wf3Pm!!,
            wf4Am = this.wf4Am!!,
            wf4Pm = this.wf4Pm!!,
            wf5Am = this.wf5Am!!,
            wf5Pm = this.wf5Pm!!,
            wf6Am = this.wf6Am!!,
            wf6Pm = this.wf6Pm!!,
            wf7Am = this.wf7Am!!,
            wf7Pm = this.wf7Pm!!,
            wf8 = this.wf8!!,
            wf9 = this.wf9!!,
            wf10 = this.wf10!!,
            refDate = this.refDate!!,
            id = this.id
        ).toMono()


    private fun MidTermForecastTempEntity.cast2MidForecastTemperaturePojo() : Mono<MidTemperatureForecast> =
        MidTemperatureForecast(
            id = this.id,
            regId = this.regId,
            refDate = this.refDate,
            taMin3 = this.taMin_3,
            taMin3Low = this.taMin_3Low,
            taMin3High = this.taMax_3High,
            taMax3 = this.taMax_3,
            taMax3Low = this.taMax_3Low,
            taMax3High = this.taMax_3High,
            taMin4 = this.taMin_4,
            taMin4Low = this.taMin_4Low,
            taMin4High = this.taMin_4High,
            taMax4 = this.taMax_4,
            taMax4Low = this.taMax_4Low,
            taMax4High = this.taMax_4High,
            taMin5 = this.taMin_5,
            taMin5Low = this.taMin_5Low,
            taMin5High = this.taMin_5High,
            taMax5 = this.taMax_5,
            taMax5Low = this.taMax_5Low,
            taMax5High = this.taMax_5High,
            taMin6 = this.taMin_6,
            taMin6Low = this.taMin_6Low,
            taMin6High = this.taMin_6High,
            taMax6 = this.taMax_6,
            taMax6Low = this.taMax_6Low,
            taMax6High = this.taMax_6High,
            taMin7 = this.taMin_7,
            taMin7Low = this.taMin_7Low,
            taMin7High = this.taMin_7High,
            taMax7 = this.taMax_7,
            taMax7Low = this.taMax_7Low,
            taMax7High = this.taMax_7High,
            taMin8 = this.taMin_8,
            taMin8Low = this.taMin_8Low,
            taMin8High = this.taMin_8High,
            taMax8 = this.taMax_8,
            taMax8Low = this.taMax_8Low,
            taMax8High = this.taMax_8High,
            taMin9 = this.taMin_9,
            taMin9Low = this.taMin_9Low,
            taMin9High = this.taMin_9High,
            taMax9 = this.taMax_9,
            taMax9Low = this.taMax_9Low,
            taMax9High = this.taMax_9High,
            taMin10 = this.taMin_10,
            taMin10Low = this.taMin_10Low,
            taMin10High =this.taMin_10High,
            taMax10 = this.taMax_10,
            taMax10Low = this.taMax_10Low,
            taMax10High = this.taMax_10High,
        ).toMono()

}
