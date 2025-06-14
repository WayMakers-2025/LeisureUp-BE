package org.leisureup.member.internal.repository;

import java.util.*;
import org.leisureup.member.internal.domain.*;
import org.springframework.data.jpa.repository.*;

public interface KakaoOAuthRepository
        extends JpaRepository<KakaoOAuth, Long> {

    @Query("""
            select mid from KakaoOAuth ko
            right join ko.member.id mid
            where ko.id = :socialId
            """)
    Optional<Long> findMemberIdBySocial(Long socialId);
}
