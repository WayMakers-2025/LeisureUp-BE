package org.leisureup.location.internal.dto.api;

import com.fasterxml.jackson.annotation.*;

@SuppressWarnings("SpellCheckingInspection")
@JsonIgnoreProperties(ignoreUnknown = true)
public record DetailedRestaurantInfo(
        @JsonProperty("chkcreditcardfood") String creditCard,
        @JsonProperty("discountinfofood") String discount,
        @JsonProperty("firstmenu") String mainMenu,
        @JsonProperty("infocenterfood") String inquiryTo,

        @JsonProperty("opentimefood") String openingFor,
        @JsonProperty("packing") String takeout,
        @JsonProperty("parkingfood") String parkingFacility,
        @JsonProperty("reservationfood") String reserveTo,

        @JsonProperty("restdatefood") String dayOff,
        @JsonProperty("scalefood") String facilitySize,

        String smoking,
        @JsonProperty("treatmenu") String menus,
        @JsonProperty("lcnsno") String licenceNo,

        @JsonProperty("kidsfacility") Integer hasChildrenPlayGround
) {

}
