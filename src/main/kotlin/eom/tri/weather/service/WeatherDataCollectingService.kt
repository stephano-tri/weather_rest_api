package eom.tri.weather.service

import eom.tri.weather.persistence.LocationShortRepository
import eom.tri.weather.persistence.ShortTermForecastEntity
import eom.tri.weather.persistence.ShortTermForecastRepository
import eom.tri.weather.util.UtilFunction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
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
     * @description 발표일 , 발표시각, 예보지점의 X,Y 좌표를 통해 조회 한 후 없으면 DB에 저장합니다.
     */

    @Scheduled(fixedDelay = 100000)
    fun collectWeatherData() {
        val fixedBaseDate = utilFunctions.toDateStr(LocalDateTime.now(), "yyyyMMdd")
        val fixedBaseTime = "0200"

        locationShortRepository.findAllDistinctPos()
            .flatMap {
                Mono.zip(it.posX.toMono() , it.posY.toMono())
            }
            .flatMap {
                requestService.getShortTermWeatherForecast(fixedBaseDate, fixedBaseTime, it.t1, it.t2)
                    .flatMap { res ->
                        Flux.fromIterable(res.response.body.items.item)
                            .flatMap { forecast ->
                                ShortTermForecastEntity(
                                    baseDate = forecast.baseDate,
                                    baseTime = forecast.baseTime,
                                    fcstDate = forecast.fcstDate,
                                    fcstTime = forecast.fcstTime,
                                    nx = forecast.nx,
                                    ny = forecast.ny,
                                    category = forecast.category,
                                    fcstValue = forecast.fcstValue,
                                ).toMono()
                            }
                            .collectList()
                            .flatMap {
                                shortTermForecastRepository.saveAll(it).collectList()
                            }
                    }
            }
            .subscribe()
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
