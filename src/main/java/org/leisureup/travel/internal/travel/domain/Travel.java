package org.leisureup.travel.internal.travel.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Travel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long travelId;

    private String travelName;

    private String travelDescription;

    private LocalDate travelDate;

    private Long memberId;

    @OneToMany(mappedBy = "travel", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @OrderBy("position")
    private List<Item> items = new ArrayList<>();

}
