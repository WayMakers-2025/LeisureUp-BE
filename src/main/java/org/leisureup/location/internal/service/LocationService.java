package org.leisureup.location.internal.service;

import lombok.*;
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

        Location location = locationRepo.findByIdFetchingCategory(locationId)
                .orElse(null);

        return Utils.buildResponse(
                location != null ?
                        location :
                        fetchService.fetchAndStoreLocation(locationId)
        );
    }
}

class Utils {

    static String emptyIfNull(String s) {
        return s != null ? s : "";
    }

    static GetLocationResponse buildResponse(Location location) {
        Long id = location.getId();
        String name = location.getTitle();
        GpsCord cord = location.getGpsCord();       // 반드시 null 아님.
        double gpsX = cord.getGpsX(), gpxY = cord.getGpsY();

        Address address = location.getAddress();    // nullable
        address = address != null ? address :
                Address.of(null, null, null);
        String add1 = address.getBriefedAddress();
        String add2 = address.getDetailedAddress();
        String zipcode = address.getZipcode();

        LocationDescription desc = location.getDescription();   // nullable
        desc = desc != null ? desc :
                LocationDescription.of(null, null, null, null, null);
        String intro = desc.getOverview();
        String homepage = desc.getHomepageInfo();
        String tel = desc.getTelephoneNumber();
        String thumb1 = desc.getLargeThumbnailUrl();
        String thumb2 = desc.getLargeThumbnailUrl();

        Category cat = location.getLocationCategory();
        String catType = cat.getCategoryType();

        return GetLocationResponse.of(
                id, name, gpsX, gpxY,
                emptyIfNull(add1), emptyIfNull(add2), emptyIfNull(zipcode),
                emptyIfNull(intro), emptyIfNull(homepage), emptyIfNull(tel),
                emptyIfNull(thumb1), emptyIfNull(thumb2),
                catType
        );
    }
}