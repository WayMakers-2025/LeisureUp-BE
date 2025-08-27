package org.leisureup.travel.internal.travel.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.leisureup.global.json.FlexibleLocalDateSerializer;
import org.leisureup.global.json.FlexibleLocalTimeSerializer;
import org.leisureup.location.spi.LocationResponse;
import org.leisureup.travel.internal.travel.domain.Item;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class LocationResponseDetail {
    private LocationResponse locationResponse;
    private int position;
    @JsonSerialize(using = FlexibleLocalTimeSerializer.class)
    private LocalTime startTime;
    @JsonSerialize(using = FlexibleLocalTimeSerializer.class)
    private LocalTime endTime;
    @JsonSerialize(using = FlexibleLocalDateSerializer.class)
    private LocalDate date;

//    public static List<LocationResponse> fromLocationResponse(List<LocationResponse> locationResponses, Item item) {
//        List<LocationResponse> locationResponseList = new ArrayList<LocationResponse>();
//        for (LocationResponse locationResponse : locationResponses){
//            LocationResponseDetail build = LocationResponseDetail.builder()
//                    .locationResponse(locationResponse)
//                    .position(item.getPosition())
//                    .startTime(item.getStartTime())
//                    .endTime(item.getEndTime())
//                    .build();
//            locationResponseList.add(build);
//        }
//
//    }
//    private LocalDate date;
}
