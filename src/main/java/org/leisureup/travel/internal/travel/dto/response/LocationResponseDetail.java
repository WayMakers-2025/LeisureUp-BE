package org.leisureup.travel.internal.travel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.leisureup.location.spi.LocationResponse;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LocationResponseDetail {
    private LocationResponse locationResponse;
    private int position;
}
