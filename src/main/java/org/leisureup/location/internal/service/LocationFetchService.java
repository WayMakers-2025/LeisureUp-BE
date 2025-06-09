package org.leisureup.location.internal.service;

import lombok.*;
import lombok.extern.slf4j.*;
import org.leisureup.global.exception.*;
import org.leisureup.location.internal.dto.response.*;
import org.leisureup.location.internal.repository.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationFetchService {

    private final LocationRepository locationRepo;

    /**
     * {@code locationId} 에 해당하는 장소를 가져오고 DB 에 저장한다.
     *
     * @param locationId 장소 id
     */
    @Transactional
    public GetLocationResponse fetchAndStoreLocation(Long locationId) {
        // TODO : 완성하기
        // TourApi 에 해당 장소가 없으면 not found
        // TourApi 응답을 받지 못하거나 에러가 발생했으면 internal server error
        // 그 외 성공적으로 가져오면 DB 에 정보 저장 필요
        throw new NotImplemented();
    }
}
