package org.leisureup.member.spi;

import java.util.*;

public interface PickSpi {

    /**
     * 장소 ID 중 사용자가 찜한 장소를 식별해 ID 로 제공
     */
    List<Long> filterPickedLocationFromMember(
            Long memberId, List<Long> locationIds
    );

}
