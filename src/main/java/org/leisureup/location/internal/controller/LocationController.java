package org.leisureup.location.internal.controller;

import jakarta.validation.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.leisureup.global.response.*;
import org.leisureup.location.internal.dto.response.*;
import org.leisureup.location.internal.service.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping("/{id}")
    public ApiResponse<GetLocationResponse> getLocation(
            @Valid @Positive @NotNull
            @PathVariable Long id
    ) {
        return ApiResponse.success(200, locationService.getLocation(id));
    }

}
