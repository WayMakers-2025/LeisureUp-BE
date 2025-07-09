package org.leisureup.travel.internal.travel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.leisureup.travel.internal.travel.domain.Travel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class GetAllTravelResponse {
    private Long travelId;

    private String travelName;

    private String travelDescription;

    private LocalDate travelDate;

    private String representImage;

    public static List<GetAllTravelResponse> fromTravel(List<Travel> travels, Map<Long, String> representImageMap) {
        List<GetAllTravelResponse> dtos = new ArrayList<>();
        for (Travel t : travels) {
            GetAllTravelResponse build = GetAllTravelResponse.builder()
                    .travelId(t.getTravelId())
                    .travelName(t.getTravelName())
                    .travelDescription(t.getTravelDescription())
                    .travelDate(t.getTravelDate())
                    .representImage(representImageMap.get(t.getTravelId()))
                    .build();
            dtos.add(build);
        }
        return dtos;
    }
}
