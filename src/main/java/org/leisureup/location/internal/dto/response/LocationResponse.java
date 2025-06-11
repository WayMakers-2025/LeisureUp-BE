package org.leisureup.location.internal.dto.response;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.leisureup.location.internal.domain.Address;
import org.leisureup.location.internal.domain.Category;
import org.leisureup.location.internal.domain.GpsCord;
import org.leisureup.location.internal.domain.LocationDescription;

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
