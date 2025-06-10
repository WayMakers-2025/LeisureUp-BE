package org.leisureup.member.internal.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * 사용자 찜 목록을 저장할 엔티티
 */
@Getter
@Entity
@IdClass(PickCompositeKey.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pick extends BaseTimeEntity {

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "a_member_id", nullable = false, updatable = false)
    private Member member;

    @Id
    @Column(name = "b_location_id", nullable = false, updatable = false)
    private long locationId;
}
