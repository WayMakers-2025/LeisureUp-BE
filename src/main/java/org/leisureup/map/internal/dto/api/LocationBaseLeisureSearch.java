package org.leisureup.map.internal.dto.api;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.*;
import com.fasterxml.jackson.databind.annotation.*;
import java.time.*;

@JsonNaming(LowerCaseStrategy.class)
public record LocationBaseLeisureSearch(
        Long contentId,
        Long contentTypeId,

        String title,
        String tel,
        String firstImage,
        String firstImage2,

        Double mapX,
        Double mapY,
        Double dist,

        @JsonProperty("addr1") String address,
        @JsonProperty("addr2") String detailedAddress,
        String zipcode,

        @JsonFormat(pattern = "yyyyMMddHHmmss") LocalDateTime createdTime,
        @JsonFormat(pattern = "yyyyMMddHHmmss") LocalDateTime modifiedTime
) {

}
