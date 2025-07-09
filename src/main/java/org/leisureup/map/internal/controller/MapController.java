package org.leisureup.map.internal.controller;

import jakarta.validation.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.leisureup.global.exception.*;
import org.leisureup.global.response.*;
import org.leisureup.map.internal.dto.*;
import org.leisureup.map.internal.dto.request.*;
import org.leisureup.map.internal.dto.response.*;
import org.leisureup.map.internal.service.*;
import org.springdoc.core.annotations.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
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
        return ApiResponse.success(
                200,
                mapService.searchCategory(x, y, radius, category)
        );
    }

    @GetMapping("/map/leisure")
    public ApiResponse<MultiPageResponse<?>>
    searchLeisureOnLocation(
            @Valid @ParameterObject
            SearchLeisureOnLocationRequest req
    ) {

        log.info("Request : {}", req.toString());

        throw new NotImplemented(
                "/map/leisure/location-base has not been implemented yet"
        );
    }
}
