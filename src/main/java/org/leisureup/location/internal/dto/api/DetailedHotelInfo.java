package org.leisureup.location.internal.dto.api;

import com.fasterxml.jackson.annotation.*;

@SuppressWarnings("SpellCheckingInspection")
@JsonIgnoreProperties(ignoreUnknown = true)
public record DetailedHotelInfo(
        @JsonProperty("accomcountlodging") String capacity,
        @JsonProperty("checkintime") String checkIn,
        @JsonProperty("checkouttime") String checkOut,

        @JsonProperty("chkcooking") String cooking,
        @JsonProperty("foodplace") String restaurantFacility,
        @JsonProperty("infocenterlodging") String inquiryTo,

        @JsonProperty("parkinglodging") String parkingFacility,
        @JsonProperty("pickup") String pickupService,
        @JsonProperty("roomcount") String numberOfRooms,

        @JsonProperty("reservationlodging") String reserveTo,
        @JsonProperty("reservationurl") String reservationHomepage,

        @JsonProperty("roomtype") String roomTypes,
        @JsonProperty("scalelodging") String facilitySize,
        @JsonProperty("subfacility") String additionalFacilities,

        @JsonProperty("refundregulation") String refundPolicy,

        @JsonProperty("barbecue") Integer hasBarbeque,
        @JsonProperty("beverage") Integer hasRestaurant,
        @JsonProperty("bicycle") Integer hasBikeRental,

        @JsonProperty("campfire") Integer hasCampFire,
        @JsonProperty("fitness") Integer hasFitnessCenter,
        @JsonProperty("karaoke") Integer hasKaraoke,

        @JsonProperty("publicbath") Integer hasPublicShowerRoom,
        @JsonProperty("publicpc") Integer hasPublicPC,
        @JsonProperty("sauna") Integer hasSauna,

        @JsonProperty("seminar") Integer hasSeminarRoom,
        @JsonProperty("sports") Integer hasSportFacility
) {

}
