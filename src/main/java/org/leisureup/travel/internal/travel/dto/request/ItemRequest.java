package org.leisureup.travel.internal.travel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ItemRequest {
    private Long locationId;
    private Integer position; // position이 null일 경우 자동으로 할당
    private LocalTime startTime;
    private LocalTime endTime;
}
