package org.leisureup.travel.internal.travel.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.leisureup.location.spi.LocationQueryPort;
import org.leisureup.travel.internal.travel.domain.Item;
import org.leisureup.travel.internal.travel.domain.Travel;
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
class TravelServiceAddItemTest {

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
    @DisplayName("addItem: 마지막 아이템의 date를 이어받고, 마지막 종료 직후 1시간으로 시간 설정")
    void addItem_assignsSameDate_and_plusOneHour_after_last() {
        // given
        Long memberId = 1L;
        Long travelId = 10L;
        Long newLocationId = 999999L;

        Travel travel = Travel.builder()
                .travelId(travelId)
                .travelName("샘플 여행")
                .memberId(memberId)
                .startDate(LocalDate.of(2025, 9, 1))
                .endDate(LocalDate.of(2025, 9, 5))
                .build();

        List<Item> existing = new ArrayList<>();
        existing.add(Item.builder()
                .locationId(111111L)
                .position(0)
                .date(LocalDate.of(2025, 9, 1))
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(10, 0))
                .travel(travel)
                .build());
        existing.add(Item.builder()
                .locationId(222222L)
                .position(1)
                .date(LocalDate.of(2025, 9, 3)) // 가장 늦은 날짜
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(16, 30))
                .travel(travel)
                .build());
        travel.getItems().addAll(existing);

        given(travelRepository.findByTravelIdAndMemberId(travelId, memberId))
                .willReturn(Optional.of(travel));

        // BEFORE
        System.out.println("[ADD-BEFORE] travelId=" + travel.getTravelId() + ", travelName=" + travel.getTravelName());
        for (Item it : travel.getItems()) {
            System.out.println("[ADD-BEFORE-ITEM] " + itemToString(it));
        }

        // when
        String result = travelService.addItem(travelId, newLocationId, memberId);

        // then
        assertThat(result).contains("성공적으로 아이템이 추가");

        // 신규 아이템 캡처
        ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
        verify(itemRepository, times(1)).save(itemCaptor.capture());
        Item saved = itemCaptor.getValue();

        // ADD-NEW + AFTER 출력
        System.out.println("[ADD-NEW-ITEM] " + itemToString(saved));
        List<Item> after = new ArrayList<>(travel.getItems());
        after.add(saved);
        System.out.println("[ADD-AFTER] count=" + after.size());
        for (Item it : after) {
            System.out.println("[ADD-AFTER-ITEM] " + itemToString(it));
        }

        // 검증: date는 마지막 date(2025-09-03)와 동일
        assertThat(saved.getDate()).isEqualTo(LocalDate.of(2025, 9, 3));
        // 검증: startTime은 마지막 endTime(16:30)과 동일, endTime은 +1시간(17:30)
        assertThat(saved.getStartTime()).isEqualTo(LocalTime.of(16, 30));
        assertThat(saved.getEndTime()).isEqualTo(LocalTime.of(17, 30));
        // position 은 기존 사이즈(2)
        assertThat(saved.getPosition()).isEqualTo(2);
        assertThat(saved.getLocationId()).isEqualTo(newLocationId);

        // 이벤트 발행 1회
        ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());
        String evStr = String.valueOf(eventCaptor.getValue());
        assertThat(evStr).contains(String.valueOf(newLocationId));
    }
}


