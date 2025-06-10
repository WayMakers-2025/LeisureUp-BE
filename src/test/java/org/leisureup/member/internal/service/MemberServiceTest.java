package org.leisureup.member.internal.service;

import static org.assertj.core.api.Assertions.*;

import lombok.extern.slf4j.*;
import org.junit.jupiter.api.*;
import org.leisureup.*;
import org.leisureup.global.exception.*;
import org.leisureup.member.internal.domain.*;
import org.leisureup.member.internal.dto.request.*;
import org.leisureup.member.internal.dto.request.SaveInterestRequest.*;
import org.leisureup.member.internal.repository.*;
import org.springframework.beans.factory.annotation.*;

@Slf4j
class MemberServiceTest extends IntegrationTestSupport {

    private static final Long invalidMemberId = Long.MAX_VALUE;
    private static Long testMemberId;
    @Autowired
    MemberService service;
    @Autowired
    MemberRepository memberRepo;
    @Autowired
    InterestRepository interestRepo;

    private static SaveInterestRequest genReq(int ageRange) {
        return new SaveInterestRequest(
                ageRange, ReqWith.alone, ReqExperience.first,
                ReqType.relieving, ReqLeisureType.any, ReqPreferredSeason.any,
                ReqDifficulty.low
        );
    }

    @BeforeEach
    void setUp() {
        testMemberId = memberRepo.save(
                Member.of("test", "test")
        ).getId();
    }

    @AfterEach
    void tearDown() {
        interestRepo.deleteAll();
        memberRepo.deleteAll();
    }

    @Test
    @DisplayName("니즈 수집 질문을 저장한다.")
    void saveInterest() {

        var req = genReq(10);

        service.saveInterest(testMemberId, req);

        // 정보가 정상적으로 저장된다.
        assertThat(interestRepo.findById(testMemberId))
                .isPresent().get()
                .hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("멤버가 존재하지 않으면 에러가 발생한다.")
    void memberNotFound() {

        var req = genReq(20);

        assertThatThrownBy(() -> service.getMember(invalidMemberId))
                .isInstanceOf(NotFound.class);
        assertThatThrownBy(() -> service.saveInterest(invalidMemberId, req))
                .isInstanceOf(NotFound.class);
    }
}