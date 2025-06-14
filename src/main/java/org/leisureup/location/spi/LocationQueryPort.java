package org.leisureup.location.spi;

import java.util.*;

public interface LocationQueryPort {

    LocationResponse getLocationById(Long locationId);

    List<LocationResponse> getLocationListById(
            List<Long> locationIds
    );

    boolean notExists(Long locationId);
}
