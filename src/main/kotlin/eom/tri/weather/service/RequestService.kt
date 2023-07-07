package eom.tri.weather.service

import eom.tri.weather.model.GovernmentAPI.GovernmentPublicAPIMidResponse
import eom.tri.weather.model.GovernmentAPI.GovernmentPublicAPIMidTmpResponse
import eom.tri.weather.model.GovernmentAPI.GovernmentPublicAPIResponse
import eom.tri.weather.model.GovernmentAPI.typed.MidTermForecast
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.util.DefaultUriBuilderFactory
import reactor.core.publisher.Mono

@Service
class RequestService {
    @Value("\${gov.api.url}")
    var govEndpoint: String? = null
    @Value("\${gov.api.key}")
    val govApiKey: String? = null

    fun getShortTermWeatherForecast(baseDate: String, baseTime: String ,posX: Int, posY: Int, totalRows: Int? = null): Mono<GovernmentPublicAPIResponse> {
        val buildFactory = DefaultUriBuilderFactory()
        buildFactory.encodingMode = DefaultUriBuilderFactory.EncodingMode.NONE
        // baseDate type yyyyMMdd , baseTime type HHmm
        return WebClient.builder()
            .uriBuilderFactory(buildFactory)
            .build()
            .get()
            .uri("$govEndpoint/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=${govApiKey}&numOfRows=${totalRows ?: 1000}&pageNo=1&dataType=JSON&base_date=${baseDate}&base_time=${baseTime}&nx=${posX}&ny=${posY}")
            .accept(org.springframework.http.MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(GovernmentPublicAPIResponse::class.java)

    }

    fun getMidTermWeatherForecast(locationId: String, announceDate: String): Mono<GovernmentPublicAPIMidResponse> {
        val buildFactory = DefaultUriBuilderFactory()
        buildFactory.encodingMode = DefaultUriBuilderFactory.EncodingMode.NONE
        // locationId: STRING , announceDate: yyyyMMddHHmm (hour allow 06 00 only)
        return WebClient.builder()
            .uriBuilderFactory(buildFactory)
            .build()
            .get()
            .uri("$govEndpoint/MidFcstInfoService/getMidLandFcst?serviceKey=${govApiKey}&numOfRows=10&pageNo=1&dataType=JSON&regId=${locationId}&tmFc=${announceDate}")
            .accept(org.springframework.http.MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(GovernmentPublicAPIMidResponse::class.java)
    }

    fun getMidTermTmpWeatherForecast(locationId: String, announceDate: String): Mono<GovernmentPublicAPIMidTmpResponse> {
        val buildFactory = DefaultUriBuilderFactory()
        buildFactory.encodingMode = DefaultUriBuilderFactory.EncodingMode.NONE
        // locationId: STRING , announceDate: yyyyMMddHHmm (hour allow 06 00 only)
        return WebClient.builder()
            .uriBuilderFactory(buildFactory)
            .build()
            .get()
            .uri("$govEndpoint/MidFcstInfoService/getMidTa?serviceKey=${govApiKey}&numOfRows=10&pageNo=1&dataType=JSON&regId=${locationId}&tmFc=${announceDate}")
            .accept(org.springframework.http.MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(GovernmentPublicAPIMidTmpResponse::class.java)

    }

    fun saveRequestLog(log : MutableMap<String, String>): Mono<String> {
        return WebClient.builder()
            .build()
            .post()
            .uri("http://elasticsearch:9200/request.log/_doc")
            .bodyValue(log)
            .retrieve()
            .bodyToMono(String::class.java)
    }


}
