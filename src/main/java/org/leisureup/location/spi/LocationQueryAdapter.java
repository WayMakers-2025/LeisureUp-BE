package org.leisureup.location.spi;

import org.leisureup.location.internal.dto.response.LocationResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LocationQueryAdapter implements LocationQueryPort{
    @Override
    public LocationResponse getLocationById(Long locationId) {
        return null;
    }

    @Override
    public List<LocationResponse> getLocationListById(List<Long> locationIds) {
        return List.of();
    }
}
