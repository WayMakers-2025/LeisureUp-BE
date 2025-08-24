package org.leisureup.info.spi.internal.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * 어느 API 의 한 구역 코드를 나타내는 entity
 */
@Getter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "code_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class RegionCode extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 구역 코드 평문
     */
    @NotBlank
    @Column(length = 16, nullable = false, unique = true)
    private String regionCode;

    /**
     * 구역 이름
     */
    @NotBlank
    @Column(length = 64)
    private String regionName;

    protected RegionCode(String regionCode, String regionName) {
        this.regionCode = regionCode;
        this.regionName = regionName;
    }
}
