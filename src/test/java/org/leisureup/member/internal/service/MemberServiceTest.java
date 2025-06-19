package org.leisureup.member.internal.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.stream.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.junit.jupiter.api.*;
import org.leisureup.*;
import org.leisureup.global.exception.*;
import org.leisureup.location.spi.*;
import org.leisureup.member.internal.domain.*;
import org.leisureup.member.internal.dto.request.*;
import org.leisureup.member.internal.dto.request.SaveInterestRequest.*;
import org.leisureup.member.internal.dto.response.*;
import org.leisureup.member.internal.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;
import org.springframework.test.context.bean.override.mockito.*;
import org.springframework.transaction.annotation.*;

@Slf4j
class MemberServiceTest extends IntegrationTestSupport {

    private static final Long invalidMemberId = Long.MAX_VALUE;
    private static Long testMemberId;
    private static final List<Long> locationIds = LongStream.rangeClosed(1, 10)
            .boxed().toList();
    private static final List<LocationResponse> locations
            = locationIds.stream()
            .map(MemberServiceTest::locationResponse)
            .toList();
    private static final Long existingLocationId = Long.MAX_VALUE / 2;
    @Autowired
    MemberService service;
    @Autowired
    MemberRepository memberRepo;
    @Autowired
    InterestRepository interestRepo;
    @Autowired
    PickRepository pickRepo;
    @Autowired
    TestInitializer testInitializer;
    @MockitoBean
    LocationQueryPort locationQueryPort;

    private static SaveInterestRequest genReq(AgeRange ageRange) {
        return new SaveInterestRequest(
                ageRange, LeisureUsual.never, PreferredStyle.relieving,
                Intensity.active, Theme.sky, AlongWith.family
        );
    }

    private static LocationResponse locationResponse(Long id) {
        return new LocationResponse(
                id, String.valueOf(id),
                null, null, null, null
        );
    }

    @BeforeEach
    void setUp() {
        Member save = testInitializer.initializeData(locationIds);

        when(locationQueryPort.notExists(anyLong()))
                .thenAnswer(invocation -> {
                    Long id = invocation.getArgument(0, Long.class);
                    return !locationIds.contains(id) && !existingLocationId.equals(id);
                });
        when(locationQueryPort.getLocationListById(locationIds))
                .thenReturn(locations);

        testMemberId = save.getId();
    }

    @AfterEach
    void tearDown() {
        interestRepo.deleteAll();
        pickRepo.deleteAll();
        memberRepo.deleteAll();
    }

    @Test
    @DisplayName("니즈 수집 질문을 저장한다.")
    void saveInterest() {

        var req = genReq(AgeRange.A_20);

        service.saveInterest(testMemberId, req);

        // 정보가 정상적으로 저장된다.
        assertThat(interestRepo.findById(testMemberId))
                .isPresent().get()
                .hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("멤버가 존재하지 않으면 에러가 발생한다.")
    void memberNotFound() {

        var req = genReq(AgeRange.A_30);

        assertThatThrownBy(() -> service.getMember(invalidMemberId))
                .isInstanceOf(NotFound.class);
        assertThatThrownBy(() -> service.saveInterest(invalidMemberId, req))
                .isInstanceOf(NotFound.class);
    }

    @Test
    @DisplayName("사용자는 찜 장소 목록을 조회할 수 있다.")
    void getPickLocations() {

        int pageNo = 0, pageSize = locationIds.size();
        PageRequest pageReq = PageRequest.of(0, pageSize);
        var response = service.getPickLocations(testMemberId, pageReq);

        assertThat(response).isNotNull();
        assertThat(response.lastPage()).isTrue();
        assertThat(response.pageNo()).isEqualTo(pageNo);
        assertThat(response.pageSize()).isEqualTo(pageSize);

        List<PickLocation> elements = response.elements();
        assertThat(elements).hasSize(pageSize);
        assertThat(elements).doesNotContainNull();
        elements.forEach(e -> assertThat(e).hasNoNullFieldsOrProperties());
    }

    @Test
    @DisplayName("사용자는 어느 장소를 찜으로 저장할 수 있다.")
    void savePickLocation() {
        var req = new SavePickLocationRequest(existingLocationId);
        service.savePickLocation(testMemberId, req);

        Member member = memberRepo.findById(testMemberId)
                .orElseThrow();
        var key = new PickCompositeKey(member, existingLocationId);

        assertThat(pickRepo.findById(key)).isPresent();
    }

    @Test
    @DisplayName("사용자는 자신의 찜 장소를 삭제할 수 있다.")
    void deletePickLocation() {
        Long locationId = locationIds.getFirst();
        service.deletePickLocation(testMemberId, locationId);

        Member member = memberRepo.findById(testMemberId)
                .orElseThrow();
        var key = new PickCompositeKey(member, locationId);

        assertThat(pickRepo.findById(key)).isEmpty();
    }

    @Test
    @DisplayName("중복된 장소는 찜 저장할 수 없다.")
    void testDuplicatePick() {
        var req = new SavePickLocationRequest(locationIds.getFirst());

        assertThatThrownBy(() -> service.savePickLocation(testMemberId, req))
                .isInstanceOf(DuplicatePickException.class);

    }

    @Test
    @DisplayName("찜 저장되지 않은 장소는 찜에서 삭제할 수 없다.")
    void testUnSavedPickDeletion() {

        assertThatThrownBy(() -> service.deletePickLocation(testMemberId, existingLocationId))
                .isInstanceOf(NotFound.class);

    }
}

@Component
@RequiredArgsConstructor
class TestInitializer {

    private final MemberRepository memberRepo;
    private final PickRepository pickRepo;

    @Transactional
    Member initializeData(List<Long> locationIds) {
        Member save = memberRepo.save(
                Member.of("test", "test")
        );

        List<Pick> picks = new ArrayList<>();
        for (Long locationId : locationIds) {
            picks.add(Pick.of(save, locationId));
        }
        pickRepo.saveAll(picks);

        return save;
    }
}