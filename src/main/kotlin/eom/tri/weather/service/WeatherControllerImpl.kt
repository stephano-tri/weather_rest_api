package eom.tri.weather.service

import eom.tri.weather.controller.WeatherController
import eom.tri.weather.exception.InvalidInputException
import eom.tri.weather.exception.NotFoundException
import eom.tri.weather.model.Address
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

        // find pos_x , pos_y by regionCode(adminDivCode)
        val searchCoordinateByRegionCode = locationShortRepository.findAllByAdminDivCode(regionCode)
            .flatMap { loc ->
                Mono.zip(loc.posX.toMono() , loc.posY.toMono())
            }

        return searchCoordinateByRegionCode
            .flatMap {
                shortTermForecastRepository.findAllByFcstDateAndNxAndNy(today, it.t1, it.t2)
                    .flatMap { shortTermForecastEn ->
                        shortTermForecastEn.apply {
                            this.category = shortTermCategoryConverter(this.category!!)
                        }.cast2ShortTermPojo()
                    }.collectList()
                    .flatMap { result ->
                        if(result.isEmpty()){
                            requestService.getShortTermWeatherForecast(today, "0200", it.t1, it.t2, 280)
                                .onErrorResume {
                                    logger.error("error : ${it.message}")
                                    NotFoundException("해당 지역의 단기예보 정보가 저장되어 있지 않아 OPEN API릁 통해 요청하였으나 요청 한도를 초과하여 실패했습니다.").toMono()
                                }
                                .flatMapIterable { it.response.body.items.item }
                                .flatMap { shortForecast ->
                                    ShortForecast(
                                        fcstTime = shortForecast.fcstTime,
                                        category = shortTermCategoryConverter(shortForecast.category),
                                        fcstValue = shortForecast.fcstValue,
                                    ).toMono()
                                }
                                .collectList()
                        }
                        else {
                            result.toMono()
                        }
                    }
            }
    }

    override fun getMidTermForecast(regionCode : String) : Mono<MidForecast> {
        val today = utilFunctions.toDateStr(LocalDateTime.now(), "yyyyMMdd")
        val fixedTime = "0600"

        val searchMidtermForecast = midTermForecastRepository.findAllByRegIdAndRefDate(regionCode, refDate = today + fixedTime)
            .flatMap { midTermForecastEn ->
                midTermForecastEn.cast2MidForecastPojo()
            }
            .switchIfEmpty(
                requestService.getMidTermWeatherForecast(regionCode, today + fixedTime)
                    .flatMapIterable {
                        it.response.body.items.item
                    }
                    .elementAt(0)
                    .flatMap {
                       convertAPIResponseToMidTermPojo(it, today)
                    }
                    .onErrorResume {
                        logger.error("error : ${it.message}")
                        NotFoundException("해당 지역의 중기예보 정보가 저장되어 있지 않아 OPEN API릁 통해 요청하였으나 요청 한도를 초과하여 실패했습니다.").toMono()
                    }
            )

        return searchMidtermForecast.elementAt(0)
    }

    override fun getMidTermForecastTemp(regionCode : String) : Mono<MidTemperatureForecast> {
        val today = utilFunctions.toDateStr(LocalDateTime.now(), "yyyyMMdd")
        val fixedTime = "0600"

        val searchMidTermTemperatureForecast = midTermForecastTempRepository.findAllByRegIdAndRefDate(regionCode, refDate = today + fixedTime)
            .flatMap { midTermForecastTempEn ->
                midTermForecastTempEn.cast2MidForecastTemperaturePojo()
            }
            .switchIfEmpty(
                requestService.getMidTermTmpWeatherForecast(regionCode, today + fixedTime)
                    .flatMapIterable {
                        it.response.body.items.item
                    }
                    .elementAt(0)
                    .flatMap {
                        convertAPIResponseToMidTermTempPojo(it, today)
                    }
                    .onErrorResume {
                        logger.error("error : ${it.message}")
                        NotFoundException("해당 지역의 중기예보 정보가 저장되어 있지 않아 OPEN API릁 통해 요청하였으나 요청 한도를 초과하여 실패했습니다.").toMono()
                    }
            )

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


    private fun convertAPIResponseToMidTermPojo(forecast: MidTermForecast , date: String): Mono<MidForecast> {
        return MidForecast(
            regId = forecast.regId,
            refDate = date,
            rnSt3Am = forecast.rnSt3Am,
            rnSt3Pm = forecast.rnSt3Pm,
            rnSt4Am = forecast.rnSt4Am,
            rnSt4Pm = forecast.rnSt4Pm,
            rnSt5Am = forecast.rnSt5Am,
            rnSt5Pm = forecast.rnSt5Pm,
            rnSt6Am = forecast.rnSt6Am,
            rnSt6Pm = forecast.rnSt6Pm,
            rnSt7Am = forecast.rnSt7Am,
            rnSt7Pm = forecast.rnSt7Pm,
            rnSt8 = forecast.rnSt8,
            rnSt9 = forecast.rnSt9,
            rnSt10 = forecast.rnSt10,
            wf3Am = forecast.wf3Am,
            wf3Pm = forecast.wf3Pm,
            wf4Am = forecast.wf4Am,
            wf4Pm = forecast.wf4Pm,
            wf5Am = forecast.wf5Am,
            wf5Pm = forecast.wf5Pm,
            wf6Am = forecast.wf6Am,
            wf6Pm = forecast.wf6Pm,
            wf7Am = forecast.wf7Am,
            wf7Pm = forecast.wf7Pm,
            wf8 = forecast.wf8,
            wf9 = forecast.wf9,
            wf10 = forecast.wf10,
        ).toMono()
    }

    private fun convertAPIResponseToMidTermTempPojo(forecast: MidTermTmpForecast, date: String): Mono<MidTemperatureForecast> {
        return MidTemperatureForecast(
            regId = forecast.regId,
            refDate = date,
            taMin3 = forecast.taMin3,
            taMin3Low = forecast.taMin3Low,
            taMin3High = forecast.taMin3High,
            taMax3 = forecast.taMax3,
            taMax3Low = forecast.taMax3Low,
            taMax3High = forecast.taMax3High,
            taMin4 = forecast.taMin4,
            taMin4Low = forecast.taMin4Low,
            taMin4High = forecast.taMin4High,
            taMax4 = forecast.taMax4,
            taMax4Low = forecast.taMax4Low,
            taMax4High = forecast.taMax4High,
            taMin5 = forecast.taMin5,
            taMin5Low = forecast.taMin5Low,
            taMin5High = forecast.taMin5High,
            taMax5 = forecast.taMax5,
            taMax5Low = forecast.taMax5Low,
            taMax5High = forecast.taMax5High,
            taMin6 = forecast.taMin6,
            taMin6Low = forecast.taMin6Low,
            taMin6High = forecast.taMin6High,
            taMax6 = forecast.taMax6,
            taMax6Low = forecast.taMax6Low,
            taMax6High = forecast.taMax6High,
            taMin7 = forecast.taMin7,
            taMin7Low = forecast.taMin7Low,
            taMin7High = forecast.taMin7High,
            taMax7 = forecast.taMax7,
            taMax7Low = forecast.taMax7Low,
            taMax7High = forecast.taMax7High,
            taMin8 = forecast.taMin8,
            taMin8Low = forecast.taMin8Low,
            taMin8High = forecast.taMin8High,
            taMax8 = forecast.taMax8,
            taMax8Low = forecast.taMax8Low,
            taMax8High = forecast.taMax8High,
            taMin9 = forecast.taMin9,
            taMin9Low = forecast.taMin9Low,
            taMin9High = forecast.taMin9High,
            taMax9 = forecast.taMax9,
            taMax9Low = forecast.taMax9Low,
            taMax9High = forecast.taMax9High,
            taMin10 = forecast.taMin10,
            taMin10Low = forecast.taMin10Low,
            taMin10High = forecast.taMin10High,
            taMax10 = forecast.taMax10,
            taMax10Low = forecast.taMax10Low,
            taMax10High = forecast.taMax10High,
        ).toMono()
    }


    private fun shortTermCategoryConverter(category: String): String {
        return when(category){
            "POP" -> "강수확률"
            "PTY" -> "강수형태"
            "R06" -> "6시간 강수량"
            "REH" -> "습도"
            "S06" -> "6시간 신적설"
            "SKY" -> "하늘상태"
            "T3H" -> "3시간 기온"
            "TMN" -> "아침 최저기온"
            "TMX" -> "낮 최고기온"
            "UUU" -> "풍속(동서성분)"
            "VVV" -> "풍속(남북성분)"
            "WAV" -> "파고"
            "VEC" -> "풍향"
            "WSD" -> "풍속"
            else -> "알수 없음"
        }
    }
}
