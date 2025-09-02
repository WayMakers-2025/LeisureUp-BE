package org.leisureup.member.internal.repository;

import java.util.*;
import org.leisureup.member.internal.domain.*;
import org.springframework.data.jpa.repository.*;

public interface KakaoOAuthRepository
        extends JpaRepository<KakaoOAuth, String> {

    @Query("""
            select m.id from KakaoOAuth ko
            inner join ko.member m
            where ko.id = :socialId
            """)
    Optional<Long> findMemberIdBySocial(String socialId);

    @Query("""
            delete KakaoOAuth ko where ko.member.id = :memberId
            """)
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    void deleteByMemberId(Long memberId);

    boolean existsByMemberId(Long memberId);
}
