package org.leisureup.member.spi.internal;

import java.util.*;
import org.leisureup.member.internal.domain.*;
import org.leisureup.member.spi.*;
import org.springframework.transaction.annotation.*;

public interface SocialAuth {

    /**
     * {@code socialId} 를 통해 DB 에 저장된 사용자 ID 를 제공
     */
    Optional<Long> findMemberIdBySocial(Long socialId);

    SocialType getSocialAuthType();

    /**
     * 소셜 인증 repository 에 해당 정보를 생성 (회원가입용)
     */
    @Transactional
    void save(Long socialId, Member member);
}
