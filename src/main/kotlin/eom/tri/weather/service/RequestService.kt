package eom.tri.weather.service

import eom.tri.weather.model.Government.GovernmentPublicAPIResponse
import eom.tri.weather.model.MidTermForecast
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.DefaultUriBuilderFactory
import reactor.core.publisher.Mono

@Service
class RequestService {
    @Value("\${gov.api.short_term_url}")
    var govShortTermEndpoint: String? = null
    @Value("\${gov.api.mid_term_url}")
    var govMidTermEndpoint: String? = null
    @Value("\${gov.api.key}")
    val govApiKey: String? = null

    fun getShortTermWeatherForecast(baseDate: String, baseTime: String ,posX: Int, posY: Int): Mono<GovernmentPublicAPIResponse> {
        val buildFactory = DefaultUriBuilderFactory()
        buildFactory.encodingMode = DefaultUriBuilderFactory.EncodingMode.NONE
        // baseDate type yyyyMMdd , baseTime type HHmm
        return WebClient.builder()
            .uriBuilderFactory(buildFactory)
            .build()
            .get()
            .uri("$govShortTermEndpoint/getVilageFcst?serviceKey=${govApiKey}&numOfRows=10&pageNo=1&dataType=JSON&base_date=${baseDate}&base_time=${baseTime}&nx=${posX}&ny=${posY}")
            .retrieve()
            .bodyToMono(GovernmentPublicAPIResponse::class.java)
    }

    fun getMidTermWeatherForecast(locationId: String, announceDate: String): Mono<MidTermForecast> {
        val buildFactory = DefaultUriBuilderFactory()
        buildFactory.encodingMode = DefaultUriBuilderFactory.EncodingMode.NONE
        // locationId: Integer , announceDate: yyyyMMddHHmm (hour allow 06 00 only)
        return WebClient.builder()
            .uriBuilderFactory(buildFactory)
            .build()
            .get()
            .uri("$govMidTermEndpoint/MidFcstInfoService/getMidTa?serviceKey=${govApiKey}&numOfRows=10&pageNo=1&regId=${locationId}&tmFc=${announceDate}")
            .retrieve()
            .bodyToMono(MidTermForecast::class.java)
    }


}
