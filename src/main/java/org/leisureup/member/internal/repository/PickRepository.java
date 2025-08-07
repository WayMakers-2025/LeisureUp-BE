package org.leisureup.member.internal.repository;

import java.util.*;
import org.leisureup.member.internal.domain.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;

public interface PickRepository extends JpaRepository<Pick, PickCompositeKey> {

    @Query(
            value = """
                    select p from Pick p
                    where p.member.id = :memberId
                    order by p.createdAt
                    """,
            countQuery = """
                    select count(p) from Pick p
                    """
    )
    Page<Pick> findAllByMemberId(Long memberId, Pageable pageable);

    @Query("""
            select p.locationId from Pick p
            where p.member.id = :memberId
            and p.locationId in :locationIds
            """)
    List<Long> filterLocationIdsInPick(Long memberId, List<Long> locationIds);
}
