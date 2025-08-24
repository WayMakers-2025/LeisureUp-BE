package org.leisureup.member.spi.internal;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import org.leisureup.member.internal.domain.*;
import org.leisureup.member.internal.repository.*;
import org.leisureup.member.spi.*;
import org.springframework.stereotype.*;

@Component
public class MemberSpiImpl implements MemberSpi {

    private final MemberRepository memberRepo;
    private final InterestRepository interestRepo;
    private final Map<SocialType, SocialAuth> socialAuths;

    public MemberSpiImpl(
            MemberRepository memberRepo, InterestRepository interestRepo,
            List<SocialAuth> socialAuths
    ) {
        this.memberRepo = memberRepo;
        this.interestRepo = interestRepo;
        this.socialAuths = socialAuths.stream().collect(
                Collectors.toMap(SocialAuth::getSocialAuthType, Function.identity())
        );
    }

    @Override
    public boolean notExists(Long memberId) {
        return !memberRepo.existsById(memberId);
    }

    @Override
    public Optional<Long> getMemberIdWithSocial(SocialType type, String socialId) {
        return socialAuths.get(type).findMemberIdBySocial(socialId);
    }

    @Override
    public Long saveNewMember(
            SocialType type, String socialId,
            String nickname, String email
    ) {
        Member save = memberRepo.save(Member.of(nickname, email));

        socialAuths.get(type).save(socialId, save);

        return save.getId();
    }

    @Override
    public Optional<InterestCode> getInterest(Long memberId) {

        Interest interest = memberId != null ?
                interestRepo.findById(memberId).orElse(null) : null;

        return Optional.ofNullable(MemberSpiUtil.toRecord(interest));
    }
}


class MemberSpiUtil {

    static InterestCode toRecord(Interest interest) {

        InterestInfo info;

        if (interest == null || (info = interest.getInfo()) == null) {
            return null;
        }

        return new InterestCode(info.getRepresentation());
    }
}