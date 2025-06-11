package org.leisureup.member.spi.internal;

import java.util.*;
import lombok.*;
import org.leisureup.member.internal.domain.*;
import org.leisureup.member.internal.repository.*;
import org.leisureup.member.spi.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

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

    @Override
    @Transactional
    public void save(Long socialId, Member member) {
        repository.save(KakaoOAuth.of(socialId, member));
    }
}
