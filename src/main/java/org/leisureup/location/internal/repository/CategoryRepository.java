package org.leisureup.location.internal.repository;

import java.util.*;
import org.leisureup.location.internal.domain.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryCode(String categoryCode);

    @Query("""
            select c from Category c
            where c.id in :categoryIds
            """)
    List<Category> findAllByCategoryId(List<Long> categoryIds);
           
    List<Category> findAllBy(Pageable pageable);
}
