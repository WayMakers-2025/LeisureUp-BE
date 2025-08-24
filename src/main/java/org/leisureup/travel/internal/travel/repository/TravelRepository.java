package org.leisureup.travel.internal.travel.repository;

import jakarta.annotation.*;
import java.util.*;
import org.leisureup.travel.internal.travel.domain.*;
import org.springframework.data.jpa.repository.*;

public interface TravelRepository extends JpaRepository<Travel, Long> {
    List<Travel> findByMemberId(Long memberId);

    Optional<Travel> findByTravelIdAndMemberId(Long travelId, Long memberId);

    @Query("""
            delete Travel t where t.travelId = :travelId
            """)
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    void deleteById(@Nonnull Long travelId);

}
