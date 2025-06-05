package org.leisureup.map.internal.controller;

import lombok.RequiredArgsConstructor;
import org.leisureup.global.response.ApiResponse;
import org.leisureup.map.internal.dto.KakaoPlaceResponse;
import org.leisureup.map.internal.service.MapService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MapController {
    private final MapService mapService;

    @GetMapping("/map/search")
    public ApiResponse<KakaoPlaceResponse> searchCategory(
            @RequestParam double x,
            @RequestParam double y,
            @RequestParam(defaultValue = "1000") int radius,
            @RequestParam String category) {
        return ApiResponse.success(mapService.searchCategory(x,y,radius,category));
    }
}
