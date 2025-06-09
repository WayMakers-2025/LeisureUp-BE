package org.leisureup.travel.internal.item.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.leisureup.travel.internal.travel.domain.Travel;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ItemId;

    @ManyToOne
    private Travel travel;
}