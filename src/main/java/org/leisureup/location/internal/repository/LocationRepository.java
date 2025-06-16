package org.leisureup.location.internal.repository;

import java.util.*;
import org.leisureup.location.internal.domain.*;
import org.springframework.data.jpa.repository.*;

public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query("""
            select l from Location l
            right join fetch l.locationCategory
            where l.id = :id
            """)
    Optional<Location> findByIdFetchingCategory(Long id);

    @Query("""
            select l from Location l
            right join fetch l.locationCategory
            where l.id in :locationIds
            """)
    List<Location> findAllByLocationIds(List<Long> locationIds);

}
