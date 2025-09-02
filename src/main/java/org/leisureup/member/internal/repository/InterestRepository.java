package org.leisureup.member.internal.repository;

import org.leisureup.member.internal.domain.*;
import org.springframework.data.jpa.repository.*;

public interface InterestRepository extends JpaRepository<Interest, Long> {

    @Query("""
            delete Interest itr where itr.member.id = :memberId
            """)
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    void deleteByMemberId(Long memberId);
}
