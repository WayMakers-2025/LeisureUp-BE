package org.leisureup.location.internal.dto.api;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.*;
import com.fasterxml.jackson.databind.annotation.*;
import java.time.*;
import org.leisureup.location.internal.domain.*;
import org.leisureup.location.internal.service.*;

/**
 * 공통정보 응답 class
 *
 * @see TourApiClient#getCommonInfo
 */
@JsonNaming(LowerCaseStrategy.class)
public record CommonInfo(
        Long contentId,
        Long contentTypeId,
        String title,
        @JsonFormat(pattern = "yyyyMMddHHmmss") LocalDateTime createdTime,
        @JsonFormat(pattern = "yyyyMMddHHmmss") LocalDateTime modifiedTime,
        String tel,
        String telName,
        String homepage,
        String firstImage,
        String firstImage2,
        String cat1,
        String cat2,
        String cat3,
        String addr1,
        String addr2,
        String zipcode,
        Double mapX,
        Double mapY,
        String overview
) {

    public GpsCord gpsCord() {
        return GpsCord.of(mapX, mapY);
    }

    public Address address() {
        return Address.of(
                addr1, addr2, zipcode
        );
    }

    public LocationDescription locationDescription() {
        return LocationDescription.of(
                overview, homepage, tel, firstImage, firstImage2
        );
    }
}
