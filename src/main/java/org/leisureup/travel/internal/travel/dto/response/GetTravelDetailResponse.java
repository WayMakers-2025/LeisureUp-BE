package org.leisureup.travel.internal.travel.dto.response;

import java.time.*;
import java.util.*;
import lombok.*;
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
    private LocalDate startDate;
    private LocalDate endDate;
    private List<LocationResponseDetail> locations;

    public static GetTravelDetailResponse fromEntity(Travel travel, List<LocationResponseDetail> locations) {

        return GetTravelDetailResponse.builder()
                .travelId(travel.getTravelId())
                .travelName(travel.getTravelName())
                .travelDescription(travel.getTravelDescription())
                .startDate(travel.getStartDate())
                .endDate(travel.getEndDate())
                .locations(locations)
                .build();
    }
}
