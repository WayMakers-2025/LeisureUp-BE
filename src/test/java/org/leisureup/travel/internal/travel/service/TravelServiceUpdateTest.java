package org.leisureup.travel.internal.travel.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.leisureup.global.response.ApiResponse;
import org.leisureup.location.spi.LocationQueryPort;
import org.leisureup.travel.internal.travel.domain.Item;
import org.leisureup.travel.internal.travel.domain.Travel;
import org.leisureup.travel.internal.travel.dto.request.CreateTravelRequest;
import org.leisureup.travel.internal.travel.dto.request.ItemRequest;
import org.leisureup.travel.internal.travel.repository.ItemRepository;
import org.leisureup.travel.internal.travel.repository.TravelRepository;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TravelServiceUpdateTest {

    @Mock private TravelRepository travelRepository;
    @Mock private ItemRepository itemRepository;
    @Mock private LocationQueryPort locationQueryPort;
    @Mock private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private TravelService travelService;

    private static String itemToString(Item it) {
        Long travelId = it.getTravel() != null ? it.getTravel().getTravelId() : null;
        return String.format(
                "id=%s, locId=%s, pos=%s, date=%s, start=%s, end=%s, travelId=%s",
                String.valueOf(it.getItemId()),
                String.valueOf(it.getLocationId()),
                String.valueOf(it.getPosition()),
                String.valueOf(it.getDate()),
                String.valueOf(it.getStartTime()),
                String.valueOf(it.getEndTime()),
                String.valueOf(travelId)
        );
    }

    @Test
    @DisplayName("updateTravel: 벌크 삭제 후 요청으로 재생성 및 반환값 확인 (컬렉션 미조작)")
    void updateTravel_replaceAll_withoutCollectionManipulation_and_returnsSuccess() {
        // given
        Long memberId = 1L;
        Long travelId = 2L;

        Travel travel = Travel.builder()
                .travelId(travelId)
                .travelName("원본")
                .memberId(memberId)
                .build();

        List<Item> existing = new ArrayList<>();
        existing.add(Item.builder()
                .locationId(2769786L)
                .position(0)
                .date(LocalDate.of(2025, 8, 31))
                .travel(travel)
                .build());
        existing.add(Item.builder()
                .locationId(2773379L)
                .position(1)
                .date(LocalDate.of(2025, 9, 2))
                .travel(travel)
                .build());
        existing.add(Item.builder()
                .locationId(2738690L)
                .position(2)
                .date(LocalDate.of(2025, 9, 1))
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(10, 0))
                .travel(travel)
                .build());
        travel.getItems().addAll(existing);

        given(travelRepository.findByTravelIdAndMemberId(travelId, memberId))
                .willReturn(Optional.of(travel));

        // 원본 콘솔 출력 (모든 필드)
        System.out.println("[BEFORE] travelId=" + travel.getTravelId() + ", travelName=" + travel.getTravelName());
        for (Item it : travel.getItems()) {
            System.out.println("[BEFORE-ITEM-ALL] " + itemToString(it));
        }

        // 요청: 2773379 제거, 2745568 추가, 2738690 시간 변경
        List<ItemRequest> reqItems = List.of(
                new ItemRequest(2769786L, 0, null, null, LocalDate.of(2025, 8, 31)),
                new ItemRequest(2738690L, 1, LocalTime.of(12, 0), LocalTime.of(12, 0), LocalDate.of(2025, 9, 2)),
                new ItemRequest(2745568L, 2, LocalTime.of(12, 0), LocalTime.of(12, 0), LocalDate.of(2025, 9, 1))
        );
        CreateTravelRequest updateReq = new CreateTravelRequest(
                "수정 테스트", "", LocalDate.of(2025, 9, 2), LocalDate.of(2025, 9, 4), reqItems
        );

        // when
        ApiResponse<String> resp = travelService.updateTravel(travelId, updateReq, memberId);

        // then - 반환값
        assertThat(resp.isSuccess()).isTrue();
        assertThat(resp.getCode()).isEqualTo(200);
        assertThat(resp.getData()).contains("성공적으로 수정");

        // 벌크 삭제 호출 검증
        verify(itemRepository, times(1)).deleteAllByTravelId(eq(travelId));

        // saveAll 캡처하여 최종 저장 목록 검증
        ArgumentCaptor<List<Item>> saveAllCaptor = ArgumentCaptor.forClass(List.class);
        verify(itemRepository, times(1)).saveAll(saveAllCaptor.capture());
        List<Item> saved = saveAllCaptor.getValue();

        // 수정후 콘솔 출력 (모든 필드: saveAll 기준)
        System.out.println("[AFTER] travelId=" + travel.getTravelId() + ", travelName=" + travel.getTravelName());
        for (Item it : saved) {
            System.out.println("[AFTER-SAVED-ITEM-ALL] " + itemToString(it));
        }

        assertThat(saved).hasSize(3);
        assertThat(saved.stream().map(Item::getLocationId).toList())
                .containsExactly(2769786L, 2738690L, 2745568L);
        assertThat(saved.get(0).getPosition()).isEqualTo(0);
        assertThat(saved.get(1).getPosition()).isEqualTo(1);
        assertThat(saved.get(2).getPosition()).isEqualTo(2);

        // 날짜/시간 반영 확인
        assertThat(saved.get(1).getDate()).isEqualTo(LocalDate.of(2025, 9, 2));
        assertThat(saved.get(1).getStartTime()).isEqualTo(LocalTime.of(12, 0));
        assertThat(saved.get(1).getEndTime()).isEqualTo(LocalTime.of(12, 0));

        assertThat(saved.get(2).getDate()).isEqualTo(LocalDate.of(2025, 9, 1));
        assertThat(saved.get(2).getStartTime()).isEqualTo(LocalTime.of(12, 0));
        assertThat(saved.get(2).getEndTime()).isEqualTo(LocalTime.of(12, 0));

        // 이벤트 발행 검증 - 3개
        ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
        verify(eventPublisher, times(3)).publishEvent(eventCaptor.capture());
        String all = eventCaptor.getAllValues().toString();
        assertThat(all).contains("2769786");
        assertThat(all).contains("2738690");
        assertThat(all).contains("2745568");

        // travel 저장 검증
        verify(travelRepository, times(1)).save(any(Travel.class));
    }
}


