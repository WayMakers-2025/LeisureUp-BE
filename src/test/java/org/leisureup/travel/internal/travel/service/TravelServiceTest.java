package org.leisureup.travel.internal.travel.service;

import static org.assertj.core.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.*;
import org.leisureup.global.*;
import org.leisureup.global.exception.*;
import org.leisureup.travel.internal.travel.domain.*;
import org.leisureup.travel.internal.travel.dto.response.*;
import org.leisureup.travel.internal.travel.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.transaction.annotation.*;

@SpringBootTest
@Transactional
@JwtAuthIfPossible
class TravelServiceTest {

    @Autowired
    private TravelService travelService;

    @Autowired
    private TravelRepository travelRepository;

    @Test
    @DisplayName("여행 목록을 정상적으로 조회한다")
    void getAllTravelSuccess() {
        // given
        Travel travel1 = Travel.builder()
                .travelName("제주도 여행")
                .travelDescription("제주도 3박 4일 여행")
                // .travelDate(LocalDate.now())
                .memberId(1L)
                .build();

        Travel travel2 = Travel.builder()
                .travelName("부산 여행")
                .travelDescription("부산 2박 3일 여행")
                // .travelDate(LocalDate.now().plusDays(1))
                .memberId(1L)
                .build();

        travelRepository.save(travel1);
        travelRepository.save(travel2);

        // when
        List<GetAllTravelResponse> result = travelService.getAllTravel(1L);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTravelName()).isEqualTo("제주도 여행");
        assertThat(result.get(1).getTravelName()).isEqualTo("부산 여행");
    }

    @Test
    @DisplayName("여행 목록이 비어있을 경우 NotFound 예외가 발생한다")
    void getAllTravelEmpty() {
        // given
        // 데이터베이스가 비어있는 상태

        // when & then
        assertThatThrownBy(() -> travelService.getAllTravel(2L))
                .isInstanceOf(NotFound.class)
                .hasMessage("생성된 여행이 없습니다.");
    }
}