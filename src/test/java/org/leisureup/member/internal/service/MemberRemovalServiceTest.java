package org.leisureup.member.internal.service;

import static org.assertj.core.api.Assertions.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.junit.jupiter.api.*;
import org.leisureup.*;
import org.leisureup.member.internal.domain.*;
import org.leisureup.member.internal.dto.request.*;
import org.leisureup.member.internal.dto.request.SaveInterestRequest.*;
import org.leisureup.member.internal.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Slf4j
class MemberRemovalServiceTest extends IntegrationTestSupport {

    private static Long testMemberId;

    @Autowired
    AppleOAuthRepository appleOAuthRepo;
    @Autowired
    GoogleOAuthRepository googleOAuthRepo;
    @Autowired
    KakaoOAuthRepository kakaoOAuthRepo;

    @Autowired
    MemberRepository memberRepo;
    @Autowired
    PickRepository pickRepo;
    @Autowired
    InterestRepository interestRepo;

    @Autowired
    MemberRemovalService memberRemovalService;

    @Autowired
    MemberRemovalServiceTestInitializer testInitializer;


    @BeforeEach
    void setUp() {
        testMemberId = memberRepo.save(
                Member.of("test", "test")
        ).getId();
    }

    @AfterEach
    void tearDown() {
        appleOAuthRepo.deleteAll();
        googleOAuthRepo.deleteAll();
        kakaoOAuthRepo.deleteAll();
        pickRepo.deleteAll();
        interestRepo.deleteAll();
        memberRepo.deleteAll();
    }

    @Test
    @DisplayName("사용자와 연관된 소셜 정보들이 삭제된다.")
    void removeRelatedOAuths() {

        testInitializer.initOAuths(testMemberId);

        List<Predicate<Long>> predicates = List.of(
                appleOAuthRepo::existsByMemberId,
                googleOAuthRepo::existsByMemberId,
                kakaoOAuthRepo::existsByMemberId
        );

        for (var pred : predicates) {
            assertThat(pred.test(testMemberId)).isTrue();
        }

        memberRemovalService.removeRelatedOAuths(testMemberId);

        for (var pred : predicates) {
            assertThat(pred.test(testMemberId)).isFalse();
        }
    }

    @Test
    @DisplayName("사용자와 연관된 찜 목록들이 삭제된다.")
    void removeRelatedPicks() {

        testInitializer.initPicks(testMemberId);

        assertThat(pickRepo.existsByMemberId(testMemberId)).isTrue();

        memberRemovalService.removeRelatedPicks(testMemberId);

        assertThat(pickRepo.existsByMemberId(testMemberId)).isFalse();
    }

    @Test
    @DisplayName("사용자와 연관된 니즈 수집 질문이 삭제된다.")
    void removeRelatedInterest() {

        testInitializer.initInterest(testMemberId);

        assertThat(interestRepo.existsById(testMemberId)).isTrue();

        memberRemovalService.removeRelatedInterest(testMemberId);

        assertThat(interestRepo.existsById(testMemberId)).isFalse();
    }
}


@Component
@RequiredArgsConstructor
class MemberRemovalServiceTestInitializer {

    private static final SaveInterestRequest GEN_REQ = new SaveInterestRequest(
            AgeRange.A_20, LeisureUsual.many, PreferredStyle.relieving,
            Intensity.light, Theme.earth, AlongWith.alone
    );
    private static final List<Long> PICK_LOC_IDS = LongStream.rangeClosed(1, 10).boxed().toList();

    private final MemberRepository memberRepo;

    private final AppleOAuthRepository appleOAuthRepo;
    private final GoogleOAuthRepository googleOAuthRepo;
    private final KakaoOAuthRepository kakaoOAuthRepo;

    private final PickRepository pickRepo;
    private final InterestRepository interestRepo;

    @Transactional
    public void initOAuths(Long memberId) {
        Member mem = find(memberId);
        appleOAuthRepo.save(AppleOAuth.of("apple", mem));
        googleOAuthRepo.save(GoogleOAuth.of("google", mem));
        kakaoOAuthRepo.save(KakaoOAuth.of("kakao", mem));
    }

    @Transactional
    public void initPicks(Long memberId) {
        Member mem = find(memberId);
        PICK_LOC_IDS.stream()
                .map(id -> Pick.of(mem, id))
                .forEach(pickRepo::save);
    }

    @Transactional
    public void initInterest(Long memberId) {
        interestRepo.save(
                Interest.of(find(memberId), InterestInfo.of(GEN_REQ))
        );
    }

    private Member find(Long memberId) {
        return memberRepo.findById(memberId)
                .orElseThrow();
    }
}