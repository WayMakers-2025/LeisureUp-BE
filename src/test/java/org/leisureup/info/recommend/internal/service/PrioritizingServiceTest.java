package org.leisureup.info.recommend.internal.service;

import static org.assertj.core.api.Assertions.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.leisureup.*;
import org.leisureup.location.spi.*;
import org.leisureup.member.spi.*;
import org.springframework.beans.factory.annotation.*;

class PrioritizingServiceTest extends IntegrationTestSupport {

    private static final int TEST_SIZE = 100;
    private static final int CODE_LEN = 10;
    private static final Random RAND = ThreadLocalRandom.current();
    private static final List<CategoryInfo> categories
            = LongStream.rangeClosed(1L, TEST_SIZE)
            .mapToObj(PrioritizingServiceTest::gen)
            .toList();
    @Autowired
    PrioritizingService service;

    private static CategoryInfo gen(long id) {
        return new CategoryInfo(
                id, String.valueOf(id), Cat.ETC,
                randomCode(CODE_LEN)
        );
    }

    private static String randomCode(int len) {
        String[] codes = new String[len];
        for (int i = 0; i < len; i++) {
            codes[i] = randomChar();
        }
        return String.join("-", codes);
    }

    private static String randomChar() {
        return String.valueOf((char) RAND.nextInt('a', 'z' + 1));
    }

    @Test
    @DisplayName("사용자 관심에 따라 점수가 큰 순으로 정렬되어 제공된다.")
    void prioritize() {

        InterestCode memberCode = new InterestCode(
                randomCode(2 * CODE_LEN)
        );

        int truncate = 10;
        var resp = service.prioritize(memberCode, categories, truncate);

        Comparator<Double> higherValueFirst = Comparator.reverseOrder();

        assertThat(resp).isNotNull().hasSizeLessThanOrEqualTo(truncate);
        assertThat(resp).extracting("score", Double.class)
                .isSortedAccordingTo(higherValueFirst);

        for (var prioritized : resp) {
            assertThat(prioritized).isNotNull().hasNoNullFieldsOrProperties();
        }
    }
}