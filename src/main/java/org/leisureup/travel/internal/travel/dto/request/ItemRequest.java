package org.leisureup.travel.internal.travel.dto.request;

import com.fasterxml.jackson.databind.annotation.*;
import jakarta.validation.constraints.*;
import java.time.*;
import lombok.*;
import org.leisureup.global.json.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ItemRequest {

    @NotNull
    @Positive
    private Long locationId;
    private Integer position; // position이 null일 경우 자동으로 할당
    @JsonDeserialize(using = FlexibleLocalTimeDeserializer.class)
    private LocalTime startTime;
    @JsonDeserialize(using = FlexibleLocalTimeDeserializer.class)
    private LocalTime endTime;
    @JsonDeserialize(using = FlexibleLocalDateDeserializer.class)
    private LocalDate date;
}
