package org.leisureup.location.internal.dto.api;

import com.fasterxml.jackson.annotation.*;

@SuppressWarnings("SpellCheckingInspection")
@JsonIgnoreProperties(ignoreUnknown = true)
public record DetailedLeisureInfo(
        @JsonProperty("accomcountleports") String capacity,
        @JsonProperty("chkbabycarriageleports") String stroller,
        @JsonProperty("chkcreditcardleports") String creditCard,

        @JsonProperty("chkpetleports") String petAccompany,
        @JsonProperty("expagerangeleports") String availableAge,

        @JsonProperty("infocenterleports") String inquiryTo,
        @JsonProperty("parkingfeeleports") String parkingFee,
        @JsonProperty("parkingleports") String parkingFacility,

        @JsonProperty("reservation") String reserveTo,
        @JsonProperty("restdateleports") String dayOff,
        @JsonProperty("scaleleports") String facilitySize,

        @JsonProperty("usefeeleports") String entranceFee,
        @JsonProperty("usetimeleports") String availableAt
) {

}
