package org.leisureup.map.internal.service;

import org.leisureup.global.response.external.tourapi.*;
import org.leisureup.map.internal.dto.api.*;
import org.springframework.cloud.openfeign.*;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "TourApiSearchClient",
        url = "${feign.tour-api.url}",
        configuration = TourApiConfig.class
)
public interface TourApiSearchClient {

    @GetMapping("/locationBasedList2")
    TourApiResponse<LocationBaseLeisureSearch> searchOnLocationBase(
            @RequestParam("serviceKey") String key,
            @RequestParam(value = "MobileApp") String app,
            @RequestParam(value = "MobileOS") String os,
            @RequestParam(value = "_type") String type,

            @RequestParam double mapX,
            @RequestParam double mapY,
            @RequestParam int radius,

            @RequestParam String cat1,
            @RequestParam String cat2,
            @RequestParam String cat3,

            @RequestParam int pageNo,
            @RequestParam int numOfRows,
            @RequestParam String arrange
    );

}
