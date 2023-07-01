package eom.tri.weather.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class RequestService {
    @Value("\${gov.api.short_term_url}")
    var govShortTermEndpoint: String? = null
    @Value("\${gov.api.mid_term_url}")
    var govMidTermEndpoint: String? = null
    @Value("\${gov.api.key}")
    val govApiKey: String? = null


    // to create webclient by baseUrl
    private fun createClient(baseUrl: String?) = WebClient.builder()
        .baseUrl(baseUrl!!)
        .build()


    fun getShortTermWeatherForecast(baseDate: String, baseTime: String ,posX: Int, posY: Int): Mono<String> {
        // baseDate type yyyyMMdd , baseTime type HHmm
        return createClient(govShortTermEndpoint)
            .get()
            .uri("/getVilageFcst?serviceKey=${govApiKey}&numOfRows=10&pageNo=1&dataType=JSON&base_date=${baseDate}&base_time=${baseTime}&nx=${posX}&ny=${posY}")
            .retrieve()
            .bodyToMono(String::class.java)
    }

    fun getMidTermWeatherForecast(locationId: String, announceDate: String): Mono<String> {
        // locationId: Integer , announceDate: yyyyMMddHHmm (hour allow 06 00 only)
        return createClient(govMidTermEndpoint)
            .get()
            .uri("/MidFcstInfoService/getMidTa?serviceKey=${govApiKey}&numOfRows=10&pageNo=1&regId=${locationId}&tmFc=${announceDate}")
            .retrieve()
            .bodyToMono(String::class.java)
    }


}
