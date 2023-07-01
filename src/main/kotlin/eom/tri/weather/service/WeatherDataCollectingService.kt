package eom.tri.weather.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class WeatherDataCollectingService {
    val logger: Logger= LoggerFactory.getLogger(WeatherDataCollectingService::class.java)


    @Scheduled(fixedDelay = 1000)
    fun collectWeatherData() {
        logger.info("collectWeatherData func executed")
    }
}
