package org.leisureup.location.internal.controller;

import jakarta.validation.constraints.*;
import lombok.*;
import org.leisureup.global.response.*;
import org.leisureup.location.internal.dto.response.*;
import org.leisureup.location.internal.service.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping("/{locationId}")
    public ApiResponse<GetLocationResponse> getLocation(
            @NotNull(message = "locationId 는 필수입니다.")
            @Positive(message = "locationId 는 0 보다 커야합니다.")
            @PathVariable Long locationId
    ) {
        return ApiResponse.success(200, locationService.getLocation(locationId));
    }

}
