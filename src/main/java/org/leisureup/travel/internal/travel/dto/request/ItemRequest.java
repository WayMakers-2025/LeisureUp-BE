package org.leisureup.travel.internal.travel.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.leisureup.global.json.FlexibleLocalDateDeserializer;
import org.leisureup.global.json.FlexibleLocalTimeDeserializer;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ItemRequest {
    private Long locationId;
    private Integer position; // position이 null일 경우 자동으로 할당
    @JsonDeserialize(using = FlexibleLocalTimeDeserializer.class)
    private LocalTime startTime;
    @JsonDeserialize(using = FlexibleLocalTimeDeserializer.class)
    private LocalTime endTime;
    @JsonDeserialize(using = FlexibleLocalDateDeserializer.class)
    private LocalDate date;
}
