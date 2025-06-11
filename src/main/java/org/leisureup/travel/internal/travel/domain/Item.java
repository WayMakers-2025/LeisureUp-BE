package org.leisureup.travel.internal.travel.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Item {
    @Id
    private Long locationId;

    private int position;

    @ManyToOne
    private Travel travel;
}