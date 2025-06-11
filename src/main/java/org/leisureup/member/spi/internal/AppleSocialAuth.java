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
public class AppleSocialAuth implements SocialAuth {

    private final AppleOAuthRepository repository;

    @Override
    public Optional<Long> findMemberIdBySocial(Long socialId) {
        return repository.findMemberIdBySocial(socialId);
    }

    @Override
    public SocialType getSocialAuthType() {
        return SocialType.APPLE;
    }

    @Override
    @Transactional
    public void save(Long socialId, Member member) {
        repository.save(AppleOAuth.of(socialId, member));
    }
}
