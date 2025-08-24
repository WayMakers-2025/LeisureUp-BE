package org.leisureup.location.internal.dto.response;

import org.leisureup.location.internal.dto.*;

public record GetLocationResponse(
        BasicLocationInfo basicLocationInfo,
        LocationType type,
        LeisureInfo leisureInfo,
        HotelInfo hotelInfo,
        RestaurantInfo restaurantInfo
) {

    public static GetLocationResponse of(
            BasicLocationInfo basicLocationInfo, LocationType type,
            LeisureInfo leisureInfo, HotelInfo hotelInfo, RestaurantInfo restaurantInfo
    ) {

        switch (type) {
            case LEISURE -> {
                hotelInfo = null;
                restaurantInfo = null;
            }
            case HOTEL -> {
                leisureInfo = null;
                restaurantInfo = null;
            }
            case RESTAURANT -> {
                leisureInfo = null;
                hotelInfo = null;
            }
            default -> {
                leisureInfo = null;
                hotelInfo = null;
                restaurantInfo = null;
            }
        }

        return new GetLocationResponse(
                basicLocationInfo, type, leisureInfo, hotelInfo, restaurantInfo
        );
    }
}
