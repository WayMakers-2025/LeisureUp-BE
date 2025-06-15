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
    private LocalDate travelDate;
    private List<LocationResponse> locations;

    public static GetTravelDetailResponse fromEntity(Travel travel, List<LocationResponse> locations) {
        return GetTravelDetailResponse.builder()
                .travelId(travel.getTravelId())
                .travelName(travel.getTravelName())
                .travelDescription(travel.getTravelDescription())
                .travelDate(travel.getTravelDate())
                .locations(locations)
                .build();
    }
}
