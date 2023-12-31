package eom.tri.weather.controller

import eom.tri.weather.model.Address
import eom.tri.weather.model.MidForecast
import eom.tri.weather.model.MidTemperatureForecast
import eom.tri.weather.model.ShortForecast
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import reactor.core.publisher.Mono

interface WeatherController {

    /**
     * @author stephano-tri
     * @description 단기예보를 조회합니다.
     * @param regionCode 검색할 지역의 코드입니다. (ex: searchAddress를 통해 얻은 code를 입력해주세요.)
     */
    @GetMapping(
        path = ["/api/v1/today/forecast/load"],
        produces = ["application/json"]
    )
    fun getTodayForecast(@RequestParam("regionCode") regionCode: String) : Mono<List<ShortForecast>>


    /**
     * @author stephano-tri
     * @description 중기예보를 조회합니다.
     * @param regionCode 검색할 지역의 코드입니다. (ex: searchAddress를 통해 얻은 code를 입력해주세요.)
     */
    @GetMapping(
        path = ["/api/v1/midterm/forecast/load"],
        produces = ["application/json"]
    )
    fun getMidTermForecast(@RequestParam("regionCode") regionCode: String) : Mono<MidForecast>

    /**
     * @author stephano-tri
     * @description 중기예보(기온)을 조회합니다.
     * @param regionCode 검색할 지역의 코드입니다. (ex: searchAddress를 통해 얻은 code를 입력해주세요.)
     */
    @GetMapping(
        path = ["/api/v1/midterm/temperature/forecast/load"],
        produces = ["application/json"]
    )
    fun getMidTermForecastTemp(@RequestParam("regionCode") regionCode: String) : Mono<MidTemperatureForecast>

    /**
     * @author stephano-tri
     * @description 예보를 조회하기 위한 주소를 검색합니다
     * @param type 검색할 주소의 타입입니다. short, mid 중 하나를 입력해야 합니다 (short 단기 예보 주소, mid 중기 예보 주소)
     *        regionName 검색할 주소의 이름입니다. (ex: 서울, 인천, 경기, 강원 등의 시,도 이름을 입력해주세요)
     */
    @GetMapping(
        path = ["/api/v1/forecast/address/search"],
    )
    fun searchAddress(@RequestParam("type") type: String,
                      @RequestParam("regionName") regionName: String) : Mono<List<Address>>
}
