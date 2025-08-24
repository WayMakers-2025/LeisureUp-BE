package org.leisureup.map.internal.service;

import org.leisureup.map.internal.dto.TourApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "tourApiLocalClient", url = "http://apis.data.go.kr")
public interface TourApiLocalClient {

    @GetMapping("/B551011/KorService2/locationBasedList2")
    TourApiResponse searchByCategory(
            @RequestParam("MobileOS") String mobileOS,
            @RequestParam("MobileApp") String mobileApp,
            @RequestParam("serviceKey") String serviceKey,
            @RequestParam("mapX") double mapX,
            @RequestParam("mapY") double mapY,
            @RequestParam("radius") int radius,
            @RequestParam(value = "_type", defaultValue = "json") String type,
            @RequestParam(value = "arrange", defaultValue = "C") String arrange,
            @RequestParam(value = "contentTypeId", required = false) Integer contentTypeId,
            @RequestParam(value = "numOfRows", defaultValue = "10") int numOfRows,
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo
    );

    @GetMapping("/B551011/KorService2/searchKeyword2")
    TourApiResponse search(
            @RequestParam("MobileOS") String mobileOS,
            @RequestParam("MobileApp") String mobileApp,
            @RequestParam("serviceKey") String serviceKey,
            @RequestParam(value = "_type", defaultValue = "json") String type,
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "numOfRows", defaultValue = "10") int numOfRows,
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo
            );

    @GetMapping("/B551011/KorService2/searchKeyword2")
    TourApiResponse searchWithRegion(
            @RequestParam("MobileOS") String mobileOS,
            @RequestParam("MobileApp") String mobileApp,
            @RequestParam("serviceKey") String serviceKey,
            @RequestParam(value = "_type", defaultValue = "json") String type,
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "lDongRegnCd", required = false) Integer lDongRegnCd,
            @RequestParam(value = "lDongSignguCd", required = false) Integer lDongSignguCd,
            @RequestParam(value = "numOfRows", defaultValue = "10") int numOfRows,
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo
    );
}
