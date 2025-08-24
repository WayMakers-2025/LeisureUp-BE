package org.leisureup.member.spi;

import static org.assertj.core.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.junit.jupiter.api.*;
import org.leisureup.*;
import org.leisureup.global.exception.*;
import org.leisureup.member.internal.domain.*;
import org.leisureup.member.internal.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Slf4j
class PickSpiTest extends IntegrationTestSupport {

    private static final List<Long> pickedLocationIds
            = LongStream.rangeClosed(1, 10).boxed().toList();
    private static final Long invalidMemberId = Long.MAX_VALUE;
    private static Long testMemberId;

    @Autowired
    PickSpiTestInitializer testInitializer;

    @Autowired
    PickSpi pickSpi;

    @Autowired
    MemberRepository memberRepo;

    @Autowired
    PickRepository pickRepo;

    @BeforeEach
    void setUp() {
        Member save = testInitializer.initializeData(pickedLocationIds);
        testMemberId = save.getId();
    }

    @AfterEach
    void tearDown() {
        pickRepo.deleteAll();
        memberRepo.deleteAll();
        testMemberId = null;
    }

    @Test
    @DisplayName("장소 중 사용자가 찜한 장소를 식별할 수 있다.")
    void filterPickedLocationFromMember() {

        List<Long> sublist = pickedLocationIds.subList(1, pickedLocationIds.size() / 2);

        List<Long> locationIds = new ArrayList<>(sublist);
        locationIds.addAll(
                List.of(Long.MAX_VALUE, Long.MAX_VALUE - 1, Long.MAX_VALUE - 2)
        );

        var resp = pickSpi.filterPickedLocationFromMember(testMemberId, locationIds);

        assertThat(resp).isNotNull()
                .hasSize(sublist.size())
                .containsAll(sublist);
    }

    @Test
    @DisplayName("사용자를 식별할 수 없으면 에러를 일으킨다.")
    void testInvalidMemberId() {

        assertThatThrownBy(() -> pickSpi.filterPickedLocationFromMember(
                invalidMemberId, pickedLocationIds
        ))
                .isInstanceOf(NotFound.class);

    }
}

@Component
@RequiredArgsConstructor
class PickSpiTestInitializer {

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
