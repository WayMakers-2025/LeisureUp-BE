package org.leisureup.location.internal.service;

import lombok.*;
import lombok.extern.slf4j.*;
import org.leisureup.location.internal.domain.*;
import org.leisureup.location.internal.dto.api.*;
import org.leisureup.location.internal.repository.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationFetchService {

    private final TourApiService tourApiService;
    private final LocationRepository locationRepo;
    private final CategoryRepository categoryRepo;

    private static Location buildEntityFromInfo(CommonInfo info) {

        GpsCord cord = info.gpsCord();
        Address address = info.address();
        LocationDescription desc = info.locationDescription();

        Location loc = Location.of(
                info.contentId(), info.contentTypeId(), info.title(),
                cord, address, desc
        );
        loc.synchronizeTo(info.modifiedTime());

        return loc;
    }

    /**
     * {@code locationId} 에 해당하는 장소를 가져오고 DB 에 저장한다.
     *
     * @param locationId 장소 id
     */
    @Transactional
    public Location fetchAndStoreLocation(Long locationId) {

        // api 로 정보를 가져온다. 실패하면 메서드에서 에러가 발생한다.
        CommonInfo fetchedInfo = tourApiService.getCommonInfo(locationId);

        String categoryCode = fetchedInfo.cat3();
        Category cat = categoryRepo.findByCategoryCode(categoryCode)
                .orElse(null);

        // 등록되지 않은 카테고리면 ETC 카테고리 생성
        if (cat == null) {
            // TODO : 카테고리 이름은 임시로 작성. 나중에 바꿀 수 있지만 api 호출 1 번 더 필요
            cat = categoryRepo.save(
                    CatOther.of(fetchedInfo.cat2(), categoryCode)
            );
        }

        // 정보를 통해 장소를 DB 에 저장한다. 카테고리도 지정해둔다.
        Location loc = buildEntityFromInfo(fetchedInfo);
        loc.changeCategory(cat);

        return locationRepo.save(loc);
    }
}
