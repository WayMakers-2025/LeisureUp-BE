package org.leisureup.location.internal.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * Location 주소
 * <li>장소마다 {@code briefedAddress} 의 표현 형식이 다를 수 있음.</li>
 * <li>{@code detailedAddress} 가 없는 장소도 있을 수 있음.</li>
 */
@Getter
@Setter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    /**
     * 간단한 주소
     * <p>
     * {@code Ex) 서울특별시 종로구 누상동}
     */
    @Column(length = 500)
    private String briefedAddress;

    /**
     * 상세 주소
     * <p>
     * {@code Ex) 산1-29번지 외}
     */
    @Column(length = 100)
    private String detailedAddress;

    /**
     * 우편번호 {@code Ex) "03038"}
     */
    @Column(length = 16)
    private String zipcode;

    public static Address of(String briefedAddress,
            String detailedAddress, String zipcode) {
        return new Address(briefedAddress, detailedAddress, zipcode);
    }

    public static Address of(Address address) {
        return of(address.getBriefedAddress(), address.getDetailedAddress(), address.getZipcode());
    }
}
