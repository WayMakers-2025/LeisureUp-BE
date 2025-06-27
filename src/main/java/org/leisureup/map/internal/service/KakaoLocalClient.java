package org.leisureup.map.internal.service;

import org.leisureup.map.internal.dto.KakaoPlaceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kakaoLocalClient", url = "https://dapi.kakao.com")
public interface KakaoLocalClient {

    @GetMapping("/v2/local/search/category.json")
    KakaoPlaceResponse searchByCategory(
            @RequestHeader("Authorization") String authorization,
            @RequestParam("category_group_code") String categoryGroupCode,
            @RequestParam("x") String longitude,
            @RequestParam("y") String latitude,
            @RequestParam("radius") int radius,
            @RequestParam(value = "sort", defaultValue = "accuracy") String sort,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    );
}