package org.leisureup.location.spi;

import lombok.*;
import org.leisureup.location.internal.domain.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LocationResponse {
    private Long id;
    private GpsCord gpsCord;
    private Address address;
    // 장소 제목 (이름)
    private String title;
    private LocationDescription description;
    private Category locationCategory;
}
