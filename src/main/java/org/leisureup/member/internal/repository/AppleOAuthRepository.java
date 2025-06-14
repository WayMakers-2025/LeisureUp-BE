package org.leisureup.member.internal.repository;

import java.util.*;
import org.leisureup.member.internal.domain.*;
import org.springframework.data.jpa.repository.*;

public interface AppleOAuthRepository
        extends JpaRepository<AppleOAuth, Long> {

    @Query("""
            select mid from AppleOAuth ao
            right join ao.member.id mid
            where ao.id = :socialId
            """)
    Optional<Long> findMemberIdBySocial(Long socialId);
}
