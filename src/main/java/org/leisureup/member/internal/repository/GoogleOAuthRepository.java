package org.leisureup.member.internal.repository;

import java.util.*;
import org.leisureup.member.internal.domain.*;
import org.springframework.data.jpa.repository.*;

public interface GoogleOAuthRepository
        extends JpaRepository<GoogleOAuth, Long> {

    @Query("""
            select m.id from GoogleOAuth go
            inner join go.member m
            where go.id = :socialId
            """)
    Optional<Long> findMemberIdBySocial(String socialId);
}
