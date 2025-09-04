package org.leisureup.travel.internal.travel.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.leisureup.travel.internal.travel.dto.request.ItemRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ItemId;

    private Long locationId;

    private int position;

    @Column(nullable = true)
    private LocalTime startTime;

    @Column(nullable = true)
    private LocalTime endTime;

    private LocalDate date;

    @ManyToOne
    private Travel travel;

    public static Item buildItem(ItemRequest itemRequest, int position, Travel travel) {
        return Item.builder()
                .locationId(itemRequest.getLocationId())
                .position(position)
                .startTime(itemRequest.getStartTime())
                .endTime(itemRequest.getEndTime())
                .travel(travel)
                .date(itemRequest.getDate())
                .build();
    }

    public static Item addItem(Long locationId,int position, Travel travel) {
        LocalDate assignedDate = null;
        LocalTime assignedStart = null;
        LocalTime assignedEnd;

        if (travel != null && travel.getItems() != null && !travel.getItems().isEmpty()) {
            // 마지막 아이템(포지션 기준)
            Item last = travel.getItems().stream()
                    .max(Comparator.comparingInt(Item::getPosition))
                    .orElse(null);

            if (last != null) {
                assignedDate = last.getDate();
                LocalTime lastEnd = last.getEndTime();
                LocalTime lastStart = last.getStartTime();
                assignedStart = lastEnd != null ? lastEnd : (lastStart != null ? lastStart : LocalTime.of(9, 0));
            }
        }

        if (assignedDate == null) {
            LocalDate base = travel != null && travel.getStartDate() != null ? travel.getStartDate() :
                    (travel != null ? travel.getEndDate() : null);
            assignedDate = base != null ? base : LocalDate.now();
        }
        if (assignedStart == null) {
            assignedStart = LocalTime.of(9, 0);
        }
        assignedEnd = assignedStart.plusHours(1);

        return Item.builder()
                .locationId(locationId)
                .position(position)
                .travel(travel)
                .date(assignedDate)
                .startTime(assignedStart)
                .endTime(assignedEnd)
                .build();
    }

    public void updatePosition(int newPosition) {
        this.position = newPosition;
    }

    public void updateDate(LocalDate newDate) {
        this.date = newDate;
    }

    public void updateTime(LocalTime newStartTime, LocalTime newEndTime) {
        this.startTime = newStartTime;
        this.endTime = newEndTime;
    }
}