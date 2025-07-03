package org.leisureup.test;

import java.util.*;
import lombok.*;
import org.leisureup.global.response.*;
import org.leisureup.test.internal.*;
import org.leisureup.test.internal.api.*;
import org.springframework.web.bind.annotation.*;

@EstimateTime
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final TestAsyncService testAsyncService;

    @GetMapping("/sync")
    public ApiResponse<List<GoogleWellKnownResp>> testSync(
            @RequestParam(defaultValue = "25") int testSize
    ) {
        return ApiResponse.success(200, testAsyncService.testSync(testSize));
    }

    @GetMapping("/async")
    public ApiResponse<List<GoogleWellKnownResp>> testAsync(
            @RequestParam(defaultValue = "25") int testSize
    ) {
        return ApiResponse.success(200, testAsyncService.testAsync(testSize));
    }
}
