package org.leisureup.member.spi.internal;

import java.util.*;
import lombok.*;
import org.leisureup.member.internal.repository.*;
import org.leisureup.member.spi.*;
import org.springframework.stereotype.*;

@Component
@RequiredArgsConstructor
public class KakaoSocialAuth implements SocialAuth {

    private final KakaoOAuthRepository repository;

    @Override
    public Optional<Long> findMemberIdBySocial(Long socialId) {
        return repository.findMemberIdBySocial(socialId);
    }

    @Override
    public SocialType getSocialAuthType() {
        return SocialType.KAKAO;
    }
}
