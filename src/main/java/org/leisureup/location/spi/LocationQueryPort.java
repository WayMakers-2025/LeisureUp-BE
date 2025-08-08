package org.leisureup.location.spi;

import org.leisureup.travel.internal.travel.domain.RegionCode;

import java.util.*;

public interface LocationQueryPort {

    /**
     * 어느 한 장소의 정보를 조회
     */
    LocationResponse getLocationById(Long locationId);

    /**
     * 제공된 ID 목록에 해당하는 장소들의 정보를 조회
     */
    List<LocationResponse> getLocationListById(
            List<Long> locationIds
    );

    /**
     * ID 에 해당하는 장소가 없는지 확인
     */
    boolean notExists(Long locationId);

    /**
     * 최대 {@code maxElements} 만큼의 장소 정보를 조회.
     * <p>
     * 조회되는 장소는 랜덤하게 변할 수 있음.
     */
    List<LocationResponse> getAnyLocations(int maxElements);

    /**
     * {@code categoryIds} 에 속한 장소 중, 최대 {@code maxElements} 만큼의 장소 정보를 조회.
     * <p>
     * 조회되는 장소는 랜덤하게 변할 수 있음.
     */
    List<LocationResponse> getAnyLocationsOnCategory(
            int maxElements, List<Long> categoryIds
    );

    String getRepresentImage(List<Long> locationId);
}
