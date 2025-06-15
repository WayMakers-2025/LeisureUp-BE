package org.leisureup.member.internal.repository;

import java.util.*;
import org.leisureup.member.internal.domain.*;
import org.springframework.data.jpa.repository.*;

public interface KakaoOAuthRepository
        extends JpaRepository<KakaoOAuth, String> {

    @Query("""
            select m.id from KakaoOAuth ko
            right join ko.member m
            where ko.id = :socialId
            """)
    Optional<Long> findMemberIdBySocial(String socialId);
}
