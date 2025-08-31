package org.leisureup.location.internal.service;

import java.time.*;
import java.util.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.leisureup.location.internal.domain.*;
import org.leisureup.location.internal.dto.api.*;
import org.leisureup.location.internal.dto.event.*;
import org.leisureup.location.internal.repository.*;
import org.springframework.context.*;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.events.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataSyncEventHandler {

    private final TourApiService tourApiService;
    private final LocationRepository locationRepo;
    private final CommonInfoCacheRepository commonInfoCacheRepo;
    private final ApplicationEventPublisher eventPublisher;

    private static void changeEntityInfo(Location location, CommonInfo info) {
        location.changeTitle(info.title());
        location.changeCord(info.gpsCord());
        location.changeAddress(info.address());
        location.changeDescription(info.locationDescription());
        location.synchronizeTo(info.modifiedTime());
    }

    @Async
    @EventListener(DataSyncCheckEvent.class)
    public void handleSyncCheckEvent(DataSyncCheckEvent event) {

        Long locationId = event.locationId();
        LocalDateTime lastModifiedTimeOnDb = event.lastModifiedAt();

        log.info("Data check event for location [{}] has been received", locationId);

        CommonInfo fetchedInfo = tourApiService.getCommonInfo(locationId);
        Object nextEvent;

        if (lastModifiedTimeOnDb.isAfter(fetchedInfo.modifiedTime())) {
            log.info("Location [{}] is up-to-date. Publishing update sync time event.", locationId);
            nextEvent = new UpdateLastSyncTimeEvent(locationId);
        } else {
            log.info("Location [{}] is out-of-date. Publishing data sync event.", locationId);
            commonInfoCacheRepo.save(CommonInfoCache.of(locationId, fetchedInfo));
            nextEvent = new DataSyncEvent(locationId);
        }

        eventPublisher.publishEvent(nextEvent);
    }

    @ApplicationModuleListener(propagation = Propagation.REQUIRES_NEW)
    public void handleUpdateLastSyncTimeEvent(UpdateLastSyncTimeEvent event) {

        Long locationId = event.locationId();

        log.info("Update last sync time event for location [{}] has been received", locationId);

        locationRepo.updateModifiedTimeFor(locationId);

        log.info("Updated last syn time.");
    }

    @ApplicationModuleListener(propagation = Propagation.REQUIRES_NEW)
    public void handleDataSyncEvent(DataSyncEvent event) {

        Long locationId = event.locationId();

        log.info("Data sync event for location [{}] has been received", locationId);

        Location target = locationRepo.findById(locationId)
                .orElseThrow(() -> {
                    String msg = String.format(
                            "Unable to find location with id [%s], fatal error",
                            locationId
                    );
                    log.error(msg);
                    return new RuntimeException(msg);
                });

        CommonInfo replace;

        Optional<CommonInfoCache> cache = commonInfoCacheRepo.findById(locationId);

        if (cache.isPresent()) {
            log.info("cache!");
            replace = cache.get().getInfo();
            commonInfoCacheRepo.delete(cache.get());
        } else {
            replace = tourApiService.getCommonInfo(locationId);
        }

        changeEntityInfo(target, replace);

        log.info("Data will be changed after transaction commit.");
    }
}
