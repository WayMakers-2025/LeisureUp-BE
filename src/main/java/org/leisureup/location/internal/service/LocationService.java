package org.leisureup.location.internal.service;

import java.util.*;
import lombok.*;
import org.leisureup.global.exception.*;
import org.leisureup.location.internal.domain.*;
import org.leisureup.location.internal.dto.response.*;
import org.leisureup.location.internal.repository.*;
import org.springframework.stereotype.*;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepo;
    private final LocationFetchService fetchService;

    /**
     * 어느 한 장소의 정보를 조회한다.
     *
     * @param locationId 장소 id
     */
    public GetLocationResponse getLocation(Long locationId) {

        Optional<Location> optional = locationRepo.findById(locationId);

        if (optional.isPresent()) {
            return buildResponse(optional.get());
        }

        return fetchService.fetchAndStoreLocation(locationId);
    }

    private GetLocationResponse buildResponse(Location location) {
        // TODO: 완성하기
        throw new NotImplemented();
    }
}
