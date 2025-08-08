package org.leisureup.travel.internal.travel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private LocalTime startTime;
    private LocalTime endTime;

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
