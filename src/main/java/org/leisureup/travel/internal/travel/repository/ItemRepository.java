package org.leisureup.travel.internal.travel.repository;

import java.util.*;
import org.leisureup.travel.internal.travel.domain.*;
import org.springframework.data.jpa.repository.*;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByTravel(Travel travel);

    @Query("""
            delete Item i where i.travel.travelId = :travelId
            """)
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    void deleteAllByTravelId(Long travelId);
}
