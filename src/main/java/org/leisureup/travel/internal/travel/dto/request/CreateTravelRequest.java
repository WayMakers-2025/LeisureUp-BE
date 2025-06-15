package org.leisureup.travel.internal.travel.dto.request;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.leisureup.travel.internal.travel.domain.Travel;

@AllArgsConstructor
@NoArgsConstructor
public class CreateTravelRequest {
    private String travelName;

    public Travel toEntity() {
        return Travel.builder()
                .travelName(travelName)
                .build();

    }
}
