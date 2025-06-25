package org.leisureup.member.internal.repository;

import java.util.*;
import org.leisureup.member.internal.domain.*;
import org.springframework.data.jpa.repository.*;

public interface AppleOAuthRepository
        extends JpaRepository<AppleOAuth, Long> {

    @Query("""
            select m.id from AppleOAuth ao
            inner join ao.member m
            where ao.id = :socialId
            """)
    Optional<Long> findMemberIdBySocial(String socialId);
}
