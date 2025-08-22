package org.leisureup.travel.internal.travel.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.leisureup.global.json.FlexibleLocalDateSerializer;
import org.leisureup.travel.internal.travel.domain.Travel;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateTravelRequest {
    private String travelName;
    private String travelDescription;
    @JsonSerialize(using = FlexibleLocalDateSerializer.class)
    private LocalDate startDate;
    @JsonSerialize(using = FlexibleLocalDateSerializer.class)
    private LocalDate endDate;
    private List<ItemRequest> items;
    
    public Travel toEntity(Long memberId) {
        return Travel.builder()
                .travelName(travelName)
                .travelDescription(travelDescription)
                .startDate(startDate)
                .endDate(endDate)
                .memberId(memberId)
                .build();
    }
}
