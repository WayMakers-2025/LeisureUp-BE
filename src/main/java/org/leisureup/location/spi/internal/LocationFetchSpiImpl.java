package org.leisureup.location.spi.internal;

import jakarta.validation.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.leisureup.global.exception.*;
import org.leisureup.location.internal.repository.*;
import org.leisureup.location.internal.service.*;
import org.leisureup.location.spi.*;
import org.springframework.modulith.events.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocationFetchSpiImpl implements LocationFetchSpi {

    private final LocationRepository locationRepo;
    private final LocationFetchService locationFetchService;

    @Override
    public boolean fetchIfLocationExists(Long locationId) {

        if (locationRepo.existsById(locationId)) {
            log.warn("Location with id {} already exists", locationId);
            return true;
        }

        try {
            locationFetchService.fetchAndStoreLocation(locationId);
            return true;
        } catch (NotFound ignored) {
        } catch (Exception e) {
            log.error(
                    "Failed to fetch info from tour api : [{}]", e.getMessage(),
                    e
            );
        }

        return false;
    }

    @Override
    @ApplicationModuleListener(propagation = Propagation.REQUIRES_NEW)
    public void onFetchLocationEvent(@Valid FetchLocationEvent event) {
        Long locationId = event.locationId();

        if (locationRepo.existsById(locationId)) {
            return;
        }

        try {
            locationFetchService.fetchAndStoreLocation(locationId);
        } catch (Exception e) {
            log.error(
                    "Failed to fetch location [{}] by [{}]",
                    e.getClass().getSimpleName(),
                    locationId
            );
        }
    }
}
