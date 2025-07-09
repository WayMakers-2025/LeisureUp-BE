package org.leisureup.map.internal.controller;

import lombok.*;
import org.leisureup.global.response.*;
import org.leisureup.map.internal.dto.*;
import org.leisureup.map.internal.service.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MapController {
    private final MapService mapService;

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
}
