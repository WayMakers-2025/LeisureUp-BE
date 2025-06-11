package org.leisureup.travel.internal.travel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.leisureup.location.internal.dto.response.LocationResponse;
import org.leisureup.travel.internal.travel.domain.Travel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class GetTravelDetailDto {
    private Long travelId;
    private String travelName;
    private String travelDescription;
    private LocalDate travelDate;
    private List<LocationResponse> locations;

    public static GetTravelDetailDto fromEntity(Travel travel, List<LocationResponse> locations) {
        return GetTravelDetailDto.builder()
                .travelId(travel.getTravelId())
                .travelName(travel.getTravelName())
                .travelDescription(travel.getTravelDescription())
                .travelDate(travel.getTravelDate())
                .locations(locations)
                .build();
    }
}
