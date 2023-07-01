package eom.tri.weather

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@EnableR2dbcRepositories
@SpringBootApplication
class WeatherApplication

fun main(args : Array<String>) {
    runApplication<WeatherApplication>(*args)
}
