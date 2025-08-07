package org.leisureup.location.internal.dto.response;

import java.io.*;

public record HotelInfo(
        String capacity,
        String checkIn,
        String checkOut,
        String cooking,
        String restaurantFacility,
        String inquiryTo,

        String parkingFacility,
        String pickupService,
        String numberOfRooms,
        String reserveTo,
        String reservationHomepage,

        String roomTypes,
        String facilitySize,
        String additionalFacilities,

        String refundPolicy,

        boolean hasBarbeque,
        boolean hasRestaurant,
        boolean hasBikeRental,

        boolean hasCampFire,
        boolean hasFitnessCenter,
        boolean hasKaraoke,

        boolean hasPublicShowerRoom,
        boolean hasPublicPC,
        boolean hasSauna,

        boolean hasSeminarRoom,
        boolean hasSportFacility
) implements Serializable {

}
