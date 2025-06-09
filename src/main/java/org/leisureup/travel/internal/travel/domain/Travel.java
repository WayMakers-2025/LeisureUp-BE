package org.leisureup.travel.internal.travel.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.leisureup.travel.internal.item.domain.Item;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Travel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long travelId;

    private String travelName;

    private LocalDate travelDate;

    @OneToMany(mappedBy = "travel", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Item> items = new ArrayList<>();

}
