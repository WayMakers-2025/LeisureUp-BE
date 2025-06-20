package org.leisureup.location.internal.repository;

import java.util.*;
import org.leisureup.location.internal.domain.*;
import org.springframework.data.domain.*;
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

    @Query("""
            select l from Location l
            right join fetch l.locationCategory
            """)
    List<Location> findAllBy(Pageable pageable);

    @Query("""
            select count(l) from Location l
            where l.locationCategory.id in :categoryIds
            """)
    long countByCategories(List<Long> categoryIds);

    @Query("""
            select l from Location l
            right join fetch l.locationCategory
            where l.locationCategory.id in :categoryIds
            """)
    List<Location> findAllByCategories(Pageable pageable, List<Long> categoryIds);
}
