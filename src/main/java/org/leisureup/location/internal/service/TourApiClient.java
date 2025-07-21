package org.leisureup.location.internal.service;

import org.leisureup.global.response.external.tourapi.*;
import org.leisureup.location.internal.dto.api.*;
import org.springframework.cloud.openfeign.*;
import org.springframework.web.bind.annotation.*;

/**
 * TourApi 에 API 요청하는 client
 */
@FeignClient(
        name = "tourApiClient",
        url = "${feign.tour-api.url}",
        configuration = DefaultTourApiErrorDecoder.class
)
public interface TourApiClient {

    @GetMapping("/detailCommon2")
    TourApiResponse<CommonInfo> getCommonInfo(
            @RequestParam("contentId") Long contentId,
            @RequestParam("serviceKey") String key,
            @RequestParam(value = "MobileApp") String app,
            @RequestParam(value = "MobileOS") String os,
            @RequestParam(value = "_type") String type
    );
}
