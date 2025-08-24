package org.leisureup.map.internal.controller;

import jakarta.validation.*;
import java.util.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.leisureup.global.response.*;
import org.leisureup.map.internal.domain.Category;
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
    public ApiResponse<PageResponse<MapResponse>> searchCategory(
            @RequestParam double x,
            @RequestParam double y,
            @RequestParam(defaultValue = "1000") int radius,
            @RequestParam Category category,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ApiResponse.success(
                200,
                mapService.searchCategory(x, y, radius, category, pageNo, pageSize)
        );
    }

    @GetMapping("/map/search")
    public ApiResponse<PageResponse<MapResponse>> search(
            @RequestParam double x,
            @RequestParam double y,
            @RequestParam String query,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ApiResponse.success(
                200,
                mapService.search(x,y,query, pageNo, pageSize)
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
        Set<Long> filters = req.getFilters();

        var resp = filters == null || filters.isEmpty() ?
                leisureSearchService.searchAnyLeisure(cordInfo, pagingInfo) :
                leisureSearchService.searchLeisureWithFilters(
                        cordInfo, pagingInfo, filters.stream().toList()
                );

        return ApiResponse.success(200, resp);
    }
}
