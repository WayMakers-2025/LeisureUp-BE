package org.leisureup.location.internal.dto.response;

import java.io.*;

public record RestaurantInfo(
        String creditCard,
        String discount,
        String mainMenu,
        String inquiryTo,

        String openingFor,
        String takeout,
        String parkingFacility,
        String reserveTo,

        String dayOff,
        String facilitySize,

        String smoking,
        String menus,
        String licenceNo,

        boolean hasChildrenPlayGround
) implements Serializable {

}
