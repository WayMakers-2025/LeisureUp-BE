package org.leisureup.location.internal.dto.response;

import java.io.*;

public record LeisureInfo(
        String capacity,
        String stroller,        // 유모차 대여 정보

        String creditCard,
        String petAccompany,    // 애완동물 동반 가능 여부

        String availableAge,
        String inquiryTo,
        String parkingFee,
        String parkingFacility,

        String reserveTo,
        String dayOff,
        String facilitySize,

        String entranceFee,
        String availableAt
) implements Serializable {

}
