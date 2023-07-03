package eom.tri.weather.service

import eom.tri.weather.model.GovernmentAPI.GovernmentPublicAPIResponse
import eom.tri.weather.persistence.LocationShortRepository
import eom.tri.weather.persistence.ShortTermForecastEntity
import eom.tri.weather.persistence.ShortTermForecastRepository
import eom.tri.weather.util.UtilFunction
import jakarta.annotation.PostConstruct
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
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
    private val shortTermForecastRepository : ShortTermForecastRepository,
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


    }


    /**
     * @author stephano-tri
     * @description 발표일 , 발표시각, 예보지점의 Code를 통하여 조회 한 후 DB에 저장합니다.(중기 온도 예보)
     */

    @PostConstruct
    @Transactional
    fun collectMidTemperatureWeatherData() {


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
