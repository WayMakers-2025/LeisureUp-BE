package org.leisureup.location.spi;

import java.util.*;
import org.springframework.stereotype.*;

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
