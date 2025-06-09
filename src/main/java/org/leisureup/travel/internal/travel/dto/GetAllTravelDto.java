package org.leisureup.travel.internal.travel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.leisureup.travel.internal.travel.domain.Travel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAllTravelDto {
    private Long travelId;

    private String travelName;

    private String travelDescription;

    private LocalDate travelDate;

    public static List<GetAllTravelDto> fromTravel(List<Travel> travels) {
        List<GetAllTravelDto> dtos = new ArrayList<>();
        for (Travel t : travels) {
            GetAllTravelDto build = GetAllTravelDto.builder()
                    .travelId(t.getTravelId())
                    .travelName(t.getTravelName())
                    .travelDescription(t.getTravelDescription())
                    .travelDate(t.getTravelDate())
                    .build();
            dtos.add(build);
        }
        return dtos;
    }
}
