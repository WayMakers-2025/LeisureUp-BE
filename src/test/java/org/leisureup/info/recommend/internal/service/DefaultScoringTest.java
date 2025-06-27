package org.leisureup.info.recommend.internal.service;

import static org.assertj.core.api.Assertions.*;

import lombok.extern.slf4j.*;
import org.junit.jupiter.api.*;

@Slf4j
class DefaultScoringTest {

    final DefaultScoring defaultScoring = new DefaultScoring();

    @Test
    @DisplayName("유사도가 높을수록 더 높은 점수를 반환한다.")
    void testScore() {

        String one = "ab-ac-ad";

        String test1 = "x-x-x";
        String test2 = "a-b-c";
        String test3 = "b-c-d";

        double score1 = defaultScoring.score(one, test1);
        double score2 = defaultScoring.score(one, test2);
        double score3 = defaultScoring.score(one, test3);

        assertThat(score1).isLessThan(score2).isLessThan(score3);
        assertThat(score2).isLessThan(score3);
    }

    @Test
    @DisplayName("문자열이 비교 불가능해도 점수는 반환한다.")
    void testIncompatible() {
        String one = "ab-ac-ad";

        String[] incompatibles = {
                "", null, "a.b?c!d", "-"
        };

        // 에러가 일어나지 않는다.
        for (String incompatible : incompatibles) {
            double score = defaultScoring.score(one, incompatible);
            log.info("Score : {}", score);
        }
    }
}
