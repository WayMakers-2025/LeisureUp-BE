package org.leisureup.location.internal.controller;

import jakarta.validation.constraints.*;
import lombok.*;
import org.leisureup.global.response.*;
import org.leisureup.location.internal.dto.*;
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
    private final AdditionalInfoService additionalInfoService;

    @GetMapping("/{locationId}")
    public ApiResponse<GetLocationResponse> getLocation(
            @NotNull(message = "locationId 는 필수입니다.")
            @Positive(message = "locationId 는 0 보다 커야합니다.")
            @PathVariable Long locationId
    ) {

        var basicLocationInfo = locationService.getLocation(locationId);
        LocationType type = basicLocationInfo.locationType();

        LeisureInfo leisureInfo = null;
        HotelInfo hotelInfo = null;
        RestaurantInfo restaurantInfo = null;

        switch (type) {
            case LEISURE -> leisureInfo
                    = additionalInfoService.getAdditionalLeisureInfo(locationId);
            case HOTEL -> hotelInfo
                    = additionalInfoService.getAdditionalHotelInfo(locationId);
            case RESTAURANT -> restaurantInfo
                    = additionalInfoService.getAdditionalRestaurantInfo(locationId);
        }

        return ApiResponse.success(
                200,
                GetLocationResponse.of(
                        basicLocationInfo, type, leisureInfo, hotelInfo, restaurantInfo
                )
        );
    }

}
