package org.leisureup.map.internal.controller;

import jakarta.validation.*;
import java.util.*;
import lombok.*;
import lombok.extern.slf4j.*;
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
    private final LeisureSearchService leisureSearchService;

    @GetMapping("/map/category")
    public ApiResponse<List<MapResponse>> searchCategory(
            @RequestParam double x,
            @RequestParam double y,
            @RequestParam(defaultValue = "1000") int radius,
            @RequestParam String category) {
        return ApiResponse.success(
                200,
                mapService.searchCategory(x, y, radius, category)
        );
    }

    @GetMapping("/map/search")
    public ApiResponse<Object> search(
            @RequestParam String query) {
        return ApiResponse.success(
                200,
                mapService.search(query)
        );
    }

    @GetMapping("/map/leisure")     // 레저 장소를 검색
    public ApiResponse<MultiPageResponse<SearchLeisureResponse>>
    searchLeisureOnLocation(
            @Valid @ParameterObject
            SearchLeisureRequest req
    ) {

        CordInfo cordInfo = req.getCordRelatedInfo();
        PagingInfo pagingInfo = req.getPagingInfo();
        Set<LeisureFilter> filters = req.getFilters();

        var resp = filters == null || filters.isEmpty() ?
                leisureSearchService.searchAnyLeisure(cordInfo, pagingInfo) :
                leisureSearchService.searchLeisureWithFilters(cordInfo, pagingInfo, filters);

        return ApiResponse.success(200, resp);
    }
}
