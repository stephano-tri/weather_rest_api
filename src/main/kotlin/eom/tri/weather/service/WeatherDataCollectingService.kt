package eom.tri.weather.service

import eom.tri.weather.model.GovernmentAPI.GovernmentPublicAPIMidResponse
import eom.tri.weather.model.GovernmentAPI.GovernmentPublicAPIMidTmpResponse
import eom.tri.weather.model.GovernmentAPI.GovernmentPublicAPIResponse
import eom.tri.weather.persistence.*
import eom.tri.weather.util.UtilFunction
import jakarta.annotation.PostConstruct
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.time.LocalDateTime

@Service
class WeatherDataCollectingService(
    private val requestService : RequestService,
    private val locationShortRepository : LocationShortRepository,
    private val locationMidRepository : LocationMidRepository,
    private val locationMidTempRepository : LocationMidTempRepository,
    private val shortTermForecastRepository : ShortTermForecastRepository,
    private val midTermForecastRepository : MidTermForecastRepository,
    private val midTermForecastTempRepository : MidTermForecastTempRepository,
    private val utilFunctions : UtilFunction,
) {
    val logger: Logger = LoggerFactory.getLogger(WeatherDataCollectingService::class.java)

    /**
     * @author stephano-tri
     * @description 발표일 , 발표시각, 예보지점의 X,Y 좌표를 통해 조회 한 후 DB에 저장합니다.(단기 예보)
     */

    @PostConstruct
    @Transactional
    fun collectWeatherData() {
        val fixedBaseDate = utilFunctions.toDateStr(LocalDateTime.now(), "yyyyMMdd")
        val fixedBaseTime = "0200"

        locationShortRepository.findAllDistinctPos()
            .flatMap {
                Mono.zip(it.posX.toMono() , it.posY.toMono())
            }
            .flatMap {
                requestService.getShortTermWeatherForecast(fixedBaseDate, fixedBaseTime, it.t1, it.t2, 200)
                    .flatMap { res ->
                        saveShortTermForecast(res)
                    }
            }
            .subscribe()
    }

    /**
     * @author stephano-tri
     * @description 발표일 , 발표시각, 예보지점의 Code를 통하여 조회 한 후 DB에 저장합니다.(중기 예보)
     */

    @PostConstruct
    @Transactional
    fun collectMidWeatherData() {
        val fixedBaseDate = utilFunctions.toDateStr(LocalDateTime.now(), "yyyyMMdd")
        val fixedBaseTime = "0600"

        locationMidRepository.findAll()
            .flatMap {
                it.code.toMono()
            }
            .flatMap { location ->
                requestService.getMidTermWeatherForecast(location, fixedBaseDate + fixedBaseTime)
                    .flatMap { res ->
                        saveMidTermForecast(res, fixedBaseDate + fixedBaseTime)
                    }
            }.subscribe()
    }


    /**
     * @author stephano-tri
     * @description 발표일 , 발표시각, 예보지점의 Code를 통하여 조회 한 후 DB에 저장합니다.(중기 온도 예보)
     */

    @PostConstruct
    @Transactional
    fun collectMidTemperatureWeatherData() {
        val fixedBaseDate = utilFunctions.toDateStr(LocalDateTime.now(), "yyyyMMdd")
        val fixedBaseTime = "0600"

        locationMidTempRepository.findAll()
            .flatMap {
                it.code.toMono()
            }
            .flatMap { location ->
                requestService.getMidTermTmpWeatherForecast(location, fixedBaseDate + fixedBaseTime)
                    .flatMap { res ->
                        saveMidTermTempForecast(res, fixedBaseDate + fixedBaseTime)
                    }
            }.subscribe()

    }


    /**
     * @author stephano-tri
     * @description 해당 날짜의 날씨를 조회하고 없을 경우 공공API에서 조회하여 DB에 저장합니다.
     */

    private fun getWeatherDataForNFE(fcstDate: String, nx: Int, ny: Int) {
        requestService.getShortTermWeatherForecast(fcstDate, "0200", nx, ny, 200)
            .flatMap { res -> saveShortTermForecast(res) }
            .subscribe()
    }

    private fun saveMidTermTempForecast(govData: GovernmentPublicAPIMidTmpResponse, refDate: String) : Mono<MutableList<MidTermForecastTempEntity>> {
        return Flux.fromIterable(govData.response.body.items.item)
            .flatMap {
                MidTermForecastTempEntity(
                    regDate = refDate,
                    regId = it.regId,
                    taMin3 = it.taMin3,
                    taMin3Low = it.taMin3Low,
                    taMin3High = it.taMin3High,
                    taMax3 = it.taMax3,
                    taMax3Low = it.taMax3Low,
                    taMax3High = it.taMax3High,
                    taMin4 = it.taMin4,
                    taMin4Low = it.taMin4Low,
                    taMin4High = it.taMin4High,
                    taMax4 = it.taMax4,
                    taMax4Low = it.taMax4Low,
                    taMax4High = it.taMax4High,
                    taMin5 = it.taMin5,
                    taMin5Low = it.taMin5Low,
                    taMin5High = it.taMin5High,
                    taMax5 = it.taMax5,
                    taMax5Low = it.taMax5Low,
                    taMax5High = it.taMax5High,
                    taMin6 = it.taMin6,
                    taMin6Low = it.taMin6Low,
                    taMin6High = it.taMin6High,
                    taMax6 = it.taMax6,
                    taMax6Low = it.taMax6Low,
                    taMax6High = it.taMax6High,
                    taMin7 = it.taMin7,
                    taMin7Low = it.taMin7Low,
                    taMin7High = it.taMin7High,
                    taMax7 = it.taMax7,
                    taMax7Low = it.taMax7Low,
                    taMax7High = it.taMax7High,
                    taMin8 = it.taMin8,
                    taMin8Low = it.taMin8Low,
                    taMin8High = it.taMin8High,
                    taMax8 = it.taMax8,
                    taMax8Low = it.taMax8Low,
                    taMax8High = it.taMax8High,
                    taMin9 = it.taMin9,
                    taMin9Low = it.taMin9Low,
                    taMin9High = it.taMin9High,
                    taMax9 = it.taMax9,
                    taMax9Low = it.taMax9Low,
                    taMax9High = it.taMax9High,
                    taMin10 = it.taMin10,
                    taMin10Low = it.taMin10Low,
                    taMin10High = it.taMin10High,
                    taMax10 = it.taMax10,
                    taMax10Low = it.taMax10Low,
                    taMax10High = it.taMax10High,
                ).toMono()
            }
            .collectList()
            .flatMap {
                midTermForecastTempRepository.saveAll(it).collectList()
            }
    }


    private fun saveMidTermForecast(govData: GovernmentPublicAPIMidResponse, refDate: String) : Mono<MutableList<MidTermForecastEntity>> {
        return Flux.fromIterable(govData.response.body.items.item)
            .flatMap { forecast ->
                MidTermForecastEntity(
                    regId = forecast.regId,
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
                    refDate = refDate
                ).toMono()
            }
            .collectList()
            .flatMap {
                midTermForecastRepository.saveAll(it).collectList()
            }
    }

    private fun saveShortTermForecast(govData: GovernmentPublicAPIResponse) : Mono<MutableList<ShortTermForecastEntity>> {
        return Flux.fromIterable(govData.response.body.items.item)
                    .flatMap { forecast ->
                        ShortTermForecastEntity(
                            baseDate=forecast.baseDate,
                            baseTime=forecast.baseTime,
                            fcstDate=forecast.fcstDate,
                            fcstTime=forecast.fcstTime,
                            nx=forecast.nx,
                            ny=forecast.ny,
                            category=forecast.category,
                            fcstValue=forecast.fcstValue,
                        ).toMono()
                    }
                    .collectList()
                    .flatMap { shortTermFcsts ->
                        shortTermForecastRepository.saveAll(shortTermFcsts)
                            .collectList()
                    }
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
