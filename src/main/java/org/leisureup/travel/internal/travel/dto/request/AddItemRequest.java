package org.leisureup.travel.internal.travel.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AddItemRequest {

    @NotNull(message = "LocationId 는 null 일 수 없습니다.")
    @Positive(message = "LocationId 는 0 보다 커야합니다.")
    private Long locationId;

}
