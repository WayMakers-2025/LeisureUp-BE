package org.leisureup.member.internal.repository;

import java.util.*;
import org.leisureup.member.internal.domain.*;
import org.springframework.data.jpa.repository.*;

public interface GoogleOAuthRepository
        extends JpaRepository<GoogleOAuth, Long> {

    @Query("""
            select mid from GoogleOAuth go
            right join go.member.id mid
            where go.id = :socialId
            """)
    Optional<Long> findMemberIdBySocial(Long socialId);
}
