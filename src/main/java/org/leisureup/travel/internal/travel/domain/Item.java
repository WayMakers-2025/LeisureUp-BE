package org.leisureup.travel.internal.travel.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.leisureup.travel.internal.travel.dto.request.ItemRequest;

import java.time.LocalTime;

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

    @ManyToOne
    private Travel travel;

    public static Item buildItem(ItemRequest itemRequest, int position, Travel travel) {
        return Item.builder()
                .locationId(itemRequest.getLocationId())
                .position(position)
                .startTime(itemRequest.getStartTime())
                .endTime(itemRequest.getEndTime())
                .travel(travel)
                .build();
    }

    public static Item addItem(Long locationId,int position, Travel travel) {
        return Item.builder()
                .locationId(locationId)
                .position(position)
                .travel(travel).build();
    }

    public void updatePosition(int newPosition) {
        this.position = newPosition;
    }
}