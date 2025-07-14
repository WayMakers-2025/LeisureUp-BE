package org.leisureup.travel.internal.travel.service;


import org.leisureup.travel.internal.travel.dto.TemperatureApiResponse;
import org.leisureup.travel.internal.travel.dto.WeatherApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "TemperatureClient", url = "http://apis.data.go.kr")
public interface Temperature {

    @GetMapping("/1360000/MidFcstInfoService/getMidTa")
    TemperatureApiResponse getTemperature(
            @RequestParam("serviceKey") String serviceKey,
            @RequestParam("numOfRows") int numOfRows,
            @RequestParam("pageNo") int pageNo,
            @RequestParam(value = "dataType", defaultValue = "JSON") String dataType,
            @RequestParam(value = "regId") String regId, // 예보구역코드
            @RequestParam(value = "tmFc") String tmFc // 발표시각
    );

    @GetMapping("/1360000/MidFcstInfoService/getMidLandFcst")
    WeatherApiResponse getWeatherDetail(
            @RequestParam("serviceKey") String serviceKey,
            @RequestParam("numOfRows") int numOfRows,
            @RequestParam("pageNo") int pageNo,
            @RequestParam(value = "dataType", defaultValue = "JSON") String dataType,
            @RequestParam(value = "regId") String regId, // 예보구역코드
            @RequestParam(value = "tmFc") String tmFc // 발표시각
    );
}
