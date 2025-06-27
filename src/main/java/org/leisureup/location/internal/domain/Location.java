package org.leisureup.location.internal.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * DB 에 저장되는 한 장소
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location extends LocationTimeStamp {

    @Id
    private Long id;

    @Embedded
    private GpsCord gpsCord;

    @Embedded
    private Address address;

    // 장소 제목 (이름)
    @Column(length = 200, nullable = false)
    private String title;

    @Embedded
    private LocationDescription description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category locationCategory;

    private Location(
            long id, String title, GpsCord gpsCord,
            Address address, LocationDescription locationDescription
    ) {
        this.id = id;
        this.title = title;
        this.gpsCord = gpsCord;
        this.address = address;
        this.description = locationDescription;
    }

    @Builder
    public static Location of(
            long id, String title, GpsCord gpsCord,
            Address address, LocationDescription description
    ) {
        return new Location(id, title, gpsCord, address, description);
    }

    public void changeCord(GpsCord cord) {
        this.gpsCord = GpsCord.of(cord);
    }

    public void changeAddress(Address address) {
        this.address = Address.of(address);
    }

    public void changeDescription(LocationDescription description) {
        this.description = LocationDescription.of(description);
    }

    public void changeCategory(Category category) {
        this.locationCategory = category;
    }
}
