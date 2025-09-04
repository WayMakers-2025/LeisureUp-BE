package org.leisureup.member.internal.service;

import lombok.*;
import org.leisureup.member.internal.repository.*;
import org.leisureup.member.spi.*;
import org.springframework.context.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Service
@RequiredArgsConstructor
public class MemberRemovalService {

    private final AppleOAuthRepository appleOAuthRepo;
    private final GoogleOAuthRepository googleOAuthRepo;
    private final KakaoOAuthRepository kakaoOAuthRepo;

    private final PickRepository pickRepo;
    private final InterestRepository interestRepo;

    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void removeRelatedOAuths(Long memberId) {
        appleOAuthRepo.deleteByMemberId(memberId);
        googleOAuthRepo.deleteByMemberId(memberId);
        kakaoOAuthRepo.deleteByMemberId(memberId);
    }

    @Transactional
    public void removeRelatedPicks(Long memberId) {
        pickRepo.deleteByMemberId(memberId);
    }

    @Transactional
    public void removeRelatedInterest(Long memberId) {
        interestRepo.deleteByMemberId(memberId);
    }

    public void publishMemberRemovalEvent(Long memberId) {
        eventPublisher.publishEvent(new MemberRemovalEvent(memberId));
    }
}
