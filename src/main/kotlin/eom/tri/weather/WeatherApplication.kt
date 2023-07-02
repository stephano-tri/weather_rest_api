package eom.tri.weather

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient

@EnableScheduling
@EnableR2dbcRepositories
@SpringBootApplication
class WeatherApplication

@Bean
fun builder(): WebClient.Builder = WebClient.builder().exchangeStrategies(
    ExchangeStrategies.builder()
        .codecs { configurer ->
            configurer
                .defaultCodecs()
                .maxInMemorySize(8 * 1024 * 1024)
        }
        .build())

fun main(args : Array<String>) {
    runApplication<WeatherApplication>(*args)
}
