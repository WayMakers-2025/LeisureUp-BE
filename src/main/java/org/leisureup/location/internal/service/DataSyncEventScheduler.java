package org.leisureup.location.internal.service;

import java.time.*;
import java.util.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.leisureup.location.internal.domain.*;
import org.leisureup.location.internal.dto.event.*;
import org.leisureup.location.internal.repository.*;
import org.springframework.context.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataSyncEventScheduler {

    private static final String AT_00_00_AT_EVERY_SUNDAY = "0 0 0 * * 0";
    private final LocationRepository locationRepo;
    private final ApplicationEventPublisher eventPublisher;

    private static LocalDateTime now() {
        return LocalDateTime.now();
    }

    private static LocalDateTime beforeAMonth(LocalDateTime localDateTime) {
        return localDateTime.minusMonths(1);
    }

    private static DataSyncCheckEvent toEvent(Location location) {
        return new DataSyncCheckEvent(
                location.getId(), location.getLastModifiedAt(), location.getLastChangedAt()
        );
    }

    @Scheduled(cron = AT_00_00_AT_EVERY_SUNDAY)
    public void publishCheckEvents() {
        log.info("Data sync event scheduler started");

        List<Location> syncCandidates = findSyncCandidates();

        log.info("[{}] candidates been found", syncCandidates.size());
        log.info("Publishing data check event.");

        syncCandidates.stream()
                .map(DataSyncEventScheduler::toEvent)
                .forEach(eventPublisher::publishEvent);
    }

    private List<Location> findSyncCandidates() {
        LocalDateTime syncCandidateModifiedTimeThreshold = beforeAMonth(now());
        return locationRepo.findAllLastModifiedBeforeThan(syncCandidateModifiedTimeThreshold);
    }
}

