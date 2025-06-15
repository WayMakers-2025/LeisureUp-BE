package org.leisureup.travel.internal.travel.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ManyToOne
    private Travel travel;

    public static Item buildItem(Long locationId, int position, Travel travel) {
        return Item.builder()
                .locationId(locationId)
                .position(position)
                .travel(travel)
                .build();
    }
}