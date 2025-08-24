package org.leisureup.location.spi.internal;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.stream.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.leisureup.global.exception.*;
import org.leisureup.location.internal.domain.*;
import org.leisureup.location.internal.repository.*;
import org.leisureup.location.spi.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocationQueryAdapter implements LocationQueryPort {

    private final LocationRepository locationRepo;

    @Override
    public LocationResponse getLocationById(Long locationId) {

        Location location = locationRepo.findByIdFetchingCategory(locationId)
                .orElseThrow(() -> new NotFound("ID 에 해당하는 장소를 찾을 수 없습니다."));

        return LocationUtils.toRecord(location);
    }

    @Override
    public List<LocationResponse> getLocationListById(List<Long> locationIds) {

        List<Location> locations = locationRepo.findAllByLocationIds(locationIds);

        if (locations.size() != locationIds.size()) {
            throw SpiUtils.throwNotFoundWithMissingIds(locationIds, locations, Location::getId);
        }

        Map<Long, LocationResponse> unorderedResp = locations.stream()
                .map(LocationUtils::toRecord)
                .collect(Collectors.toMap(LocationResponse::locationId, Function.identity()));

        return locationIds.stream()
                .map(unorderedResp::get)
                .toList();
    }

    @Override
    public boolean notExists(Long locationId) {
        return !locationRepo.existsById(locationId);
    }

    @Override
    public List<LocationResponse> getAnyLocations(int maxElements) {
        long cnt = locationRepo.count();

        if (cnt == 0) {
            return Collections.emptyList();
        }

        List<Location> locations = locationRepo.findAllBy(
                LocationUtils.randomPageRequest(maxElements, cnt)
        );

        return locations.stream()
                .map(LocationUtils::toRecord)
                .toList();
    }

    @Override
    public List<LocationResponse> getAnyLocationsOnCategory(
            int maxElements, List<Long> categoryIds
    ) {
        long cnt = locationRepo.countByCategories(categoryIds);

        if (cnt == 0) {
            return Collections.emptyList();
        }

        List<Location> locations = locationRepo.findAllByCategories(
                LocationUtils.randomPageRequest(maxElements, cnt), categoryIds
        );

        return locations.stream()
                .map(LocationUtils::toRecord)
                .toList();
    }

    @Override
    public String getRepresentImage(List<Long> locationIds) {
        List<LocationResponse> locations = new ArrayList<>(locationIds.size());

        try {
            locations.addAll(this.getLocationListById(locationIds));
        } catch (NotFound ignored) {
            // ignored
        } catch (Exception e) {
            log.warn(
                    "Unexpected error [{}] occurred while try to get represent image with ids: [{}]",
                    e.getClass().getSimpleName(), locationIds, e
            );
        }

        return locations.stream()
                .map(LocationResponse::description)
                .filter(Objects::nonNull)
                .map(LocationUtils::getFirstAvailableThumbnail)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse("");
    }
}

class LocationUtils {

    private static final Random RAND = ThreadLocalRandom.current();

    static PageRequest randomPageRequest(
            int pageSize, long totalElements
    ) {
        int total = totalElements > Integer.MAX_VALUE ?
                Integer.MAX_VALUE : (int) totalElements;

        int totalPages = Math.ceilDiv(total, pageSize);
        int randomPage = RAND.nextInt(totalPages);

        return PageRequest.of(randomPage, pageSize);
    }

    static LocationResponse toRecord(Location location) {
        Long locationId = location.getId();
        String title = location.getTitle();
        GpsCord cord = location.getGpsCord();

        Gps gps = new Gps(cord.getGpsX(), cord.getGpsY());
        Add add = toRecord(location.getAddress());
        Desc desc = toRecord(location.getDescription());
        Cat cat = resolveCategory(location.getLocationCategory());

        return new LocationResponse(
                locationId, title, gps,
                add, desc, cat
        );
    }

    static Add toRecord(Address address) {

        String add1, add2, zip;
        add1 = add2 = zip = null;

        if (address != null) {
            add1 = address.getBriefedAddress();
            add2 = address.getDetailedAddress();
            zip = address.getZipcode();
        }

        return Add.of(add1, add2, zip);
    }

    static Desc toRecord(LocationDescription description) {

        String ov, hm, tel, lt, st;
        ov = hm = tel = lt = st = null;

        if (description != null) {
            ov = description.getOverview();
            hm = description.getHomepageInfo();
            tel = description.getTelephoneNumber();
            lt = description.getLargeThumbnailUrl();
            st = description.getSmallThumbnailUrl();
        }

        return Desc.of(ov, hm, tel, lt, st);
    }

    static Cat resolveCategory(Category category) {
        String type = category.getCategoryType();
        return switch (type) {
            case CatEarth.TYPE -> Cat.EARTH;
            case CatWater.TYPE -> Cat.WATER;
            case CatSky.TYPE -> Cat.SKY;
            default -> Cat.ETC;
        };
    }

    static String getFirstAvailableThumbnail(Desc desc) {
        return desc == null ? "" :
                Optional.ofNullable(desc.largeThumbnail())
                        .orElse(desc.smallThumbnail());
    }
}