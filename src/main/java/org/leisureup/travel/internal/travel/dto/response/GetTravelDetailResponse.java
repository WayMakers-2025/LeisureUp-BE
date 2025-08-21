package org.leisureup.travel.internal.travel.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.*;
import java.util.*;
import lombok.*;
import org.leisureup.global.json.FlexibleLocalDateSerializer;
import org.leisureup.location.spi.*;
import org.leisureup.travel.internal.travel.domain.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class GetTravelDetailResponse {
    private Long travelId;
    private String travelName;
    private String travelDescription;
    @JsonSerialize(using = FlexibleLocalDateSerializer.class)
    private LocalDate startDate;
    @JsonSerialize(using = FlexibleLocalDateSerializer.class)
    private LocalDate endDate;
    private String representImage;
    private List<LocationResponseDetail> locations;

    public static GetTravelDetailResponse fromEntity(Travel travel, String representImage,
                                                     List<LocationResponseDetail> locations) {
        return GetTravelDetailResponse.builder()
                .travelId(travel.getTravelId())
                .travelName(travel.getTravelName())
                .travelDescription(travel.getTravelDescription())
                .startDate(travel.getStartDate())
                .endDate(travel.getEndDate())
                .representImage(representImage)
                .locations(locations)
                .build();
    }
}
