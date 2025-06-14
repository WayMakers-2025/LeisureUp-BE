package org.leisureup.member.spi.internal;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import org.leisureup.member.internal.repository.*;
import org.leisureup.member.spi.*;
import org.springframework.stereotype.*;

@Component
public class MemberSpiImpl implements MemberSpi {

    private final MemberRepository memberRepo;
    private final Map<SocialType, SocialAuth> socialAuths;

    public MemberSpiImpl(
            MemberRepository memberRepo,
            List<SocialAuth> socialAuths
    ) {
        this.memberRepo = memberRepo;
        this.socialAuths = socialAuths.stream().collect(
                Collectors.toMap(SocialAuth::getSocialAuthType, Function.identity())
        );
    }

    @Override
    public boolean notExists(Long memberId) {
        return !memberRepo.existsById(memberId);
    }

    @Override
    public Optional<Long> getMemberIdWithSocial(SocialType type, Long socialId) {
        return socialAuths.get(type).findMemberIdBySocial(socialId);
    }
}
