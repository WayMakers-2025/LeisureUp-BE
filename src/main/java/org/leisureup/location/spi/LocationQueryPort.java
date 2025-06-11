package org.leisureup.location.spi;

import org.leisureup.location.internal.dto.response.LocationResponse;

import java.util.List;

public interface LocationQueryPort {
    LocationResponse getLocationById(Long locationId);
    List<LocationResponse> getLocationListById(List<Long> locationIds);
}
    