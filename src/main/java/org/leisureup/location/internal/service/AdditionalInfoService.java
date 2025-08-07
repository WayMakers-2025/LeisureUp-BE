package org.leisureup.location.internal.service;

import lombok.*;
import org.leisureup.location.internal.dto.api.*;
import org.leisureup.location.internal.dto.response.*;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.*;

@Service
@RequiredArgsConstructor
public class AdditionalInfoService {

    private final TourApiService tourApiService;

    /**
     * 레저 장소에 대한 추가 정보를 조회한다.
     */
    @Cacheable(
            cacheNames = "additional-leisure-info",
            key = "#locationId"
    )
    public LeisureInfo getAdditionalLeisureInfo(Long locationId) {
        return AdditionalInfoServiceUtils.buildResp(
                tourApiService.getDetailedLeisureInfo(locationId)
        );
    }

    /**
     * 숙박 장소에 대한 추가 정보를 조회한다.
     */
    @Cacheable(
            cacheNames = "additional-hotel-info",
            key = "#locationId"
    )
    public HotelInfo getAdditionalHotelInfo(Long locationId) {
        return AdditionalInfoServiceUtils.buildResp(
                tourApiService.getDetailedHotelInfo(locationId)
        );
    }

    /**
     * 음식점 장소에 대한 추가 정보를 조회한다.
     */
    @Cacheable(
            cacheNames = "additional-restaurant-info",
            key = "#locationId"
    )
    public RestaurantInfo getAdditionalRestaurantInfo(Long locationId) {
        return AdditionalInfoServiceUtils.buildResp(
                tourApiService.getDetailedRestaurantInfo(locationId)
        );
    }
}

class AdditionalInfoServiceUtils {

    private static String emptyIfNull(String s) {
        return s == null ? "" : s;
    }

    private static boolean resolveBoolean(Integer val) {
        return val != null && val == 1;
    }

    static LeisureInfo buildResp(DetailedLeisureInfo detailedInfo) {
        String capacity = emptyIfNull(detailedInfo.capacity());
        String stroller = emptyIfNull(detailedInfo.stroller());

        String creditCard = emptyIfNull(detailedInfo.creditCard());
        String petAccompany = emptyIfNull(detailedInfo.petAccompany());

        String availableAge = emptyIfNull(detailedInfo.availableAge());
        String inquiryTo = emptyIfNull(detailedInfo.inquiryTo());
        String parkingFee = emptyIfNull(detailedInfo.parkingFee());
        String parkingFacility = emptyIfNull(detailedInfo.parkingFacility());

        String reserveTo = emptyIfNull(detailedInfo.reserveTo());
        String dayOff = emptyIfNull(detailedInfo.dayOff());
        String facilitySize = emptyIfNull(detailedInfo.facilitySize());

        String entranceFee = emptyIfNull(detailedInfo.entranceFee());
        String availableAt = emptyIfNull(detailedInfo.availableAt());

        return new LeisureInfo(
                capacity, stroller, creditCard, petAccompany,
                availableAge, inquiryTo, parkingFee, parkingFacility,
                reserveTo, dayOff, facilitySize, entranceFee, availableAt
        );
    }

    static HotelInfo buildResp(DetailedHotelInfo detailedInfo) {
        String capacity = emptyIfNull(detailedInfo.capacity());
        String checkIn = emptyIfNull(detailedInfo.checkIn());
        String checkOut = emptyIfNull(detailedInfo.checkOut());
        String cooking = emptyIfNull(detailedInfo.cooking());

        String restaurantFacility = emptyIfNull(detailedInfo.restaurantFacility());
        String inquiryTo = emptyIfNull(detailedInfo.inquiryTo());

        String parkingFacility = emptyIfNull(detailedInfo.parkingFacility());
        String pickupService = emptyIfNull(detailedInfo.pickupService());
        String numberOfRooms = emptyIfNull(detailedInfo.numberOfRooms());

        String reserveTo = emptyIfNull(detailedInfo.reserveTo());
        String reservationHomepage = emptyIfNull(detailedInfo.reservationHomepage());

        String roomType = emptyIfNull(detailedInfo.roomTypes());
        String facilitySize = emptyIfNull(detailedInfo.facilitySize());
        String additionalFacilities = emptyIfNull(detailedInfo.additionalFacilities());
        String refundPolicy = emptyIfNull(detailedInfo.refundPolicy());

        boolean hasBarbeque = resolveBoolean(detailedInfo.hasBarbeque());
        boolean hasRestaurant = resolveBoolean(detailedInfo.hasRestaurant());
        boolean hasBikeRental = resolveBoolean(detailedInfo.hasBikeRental());

        boolean hasCampFire = resolveBoolean(detailedInfo.hasCampFire());
        boolean hasFitnessCenter = resolveBoolean(detailedInfo.hasFitnessCenter());
        boolean hasKaraoke = resolveBoolean(detailedInfo.hasKaraoke());

        boolean hasPublicShowerRoom = resolveBoolean(detailedInfo.hasPublicShowerRoom());
        boolean hasPublicPC = resolveBoolean(detailedInfo.hasPublicPC());
        boolean hasSauna = resolveBoolean(detailedInfo.hasSauna());

        boolean hasSeminarRoom = resolveBoolean(detailedInfo.hasSeminarRoom());
        boolean hasSportFacility = resolveBoolean(detailedInfo.hasSportFacility());

        return new HotelInfo(
                capacity, checkIn, checkOut, cooking, restaurantFacility, inquiryTo,
                parkingFacility, pickupService, numberOfRooms,
                reserveTo, reservationHomepage, roomType, facilitySize,
                additionalFacilities, refundPolicy,
                hasBarbeque, hasRestaurant, hasBikeRental,
                hasCampFire, hasFitnessCenter, hasKaraoke,
                hasPublicShowerRoom, hasPublicPC, hasSauna,
                hasSeminarRoom, hasSportFacility
        );
    }

    static RestaurantInfo buildResp(DetailedRestaurantInfo detailedInfo) {
        String creditCard = emptyIfNull(detailedInfo.creditCard());
        String discount = emptyIfNull(detailedInfo.discount());
        String mainMenu = emptyIfNull(detailedInfo.mainMenu());
        String inquiryTo = emptyIfNull(detailedInfo.inquiryTo());

        String openingFor = emptyIfNull(detailedInfo.openingFor());
        String takeout = emptyIfNull(detailedInfo.takeout());
        String parkingFacility = emptyIfNull(detailedInfo.parkingFacility());
        String reserveTo = emptyIfNull(detailedInfo.reserveTo());

        String dayOff = emptyIfNull(detailedInfo.dayOff());
        String facilitySize = emptyIfNull(detailedInfo.facilitySize());

        String smoking = emptyIfNull(detailedInfo.smoking());
        String menus = emptyIfNull(detailedInfo.menus());
        String licenceNo = emptyIfNull(detailedInfo.licenceNo());

        boolean hasChildrenPlayGround = resolveBoolean(detailedInfo.hasChildrenPlayGround());

        return new RestaurantInfo(
                creditCard, discount, mainMenu, inquiryTo,
                openingFor, takeout, parkingFacility, reserveTo,
                dayOff, facilitySize, smoking, menus, licenceNo,
                hasChildrenPlayGround
        );
    }
}
