package org.leisureup.location.internal.domain;

import jakarta.persistence.*;
import java.time.*;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.*;

/**
 * 어느 장소에 대한 정보 동기화를 위한 timestamp
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class LocationTimeStamp {

    /**
     * DB 에 정보가 생성된 시각
     */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime storedAt;

    /**
     * 우리 DB 가 마지막으로 동기화한 시각
     */
    @CreatedDate
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime lastModifiedAt;

    /**
     * Api 정보가 마지막으로 수정된 시각
     * <p>
     * {@code TourApi} 의 {@code modifiedtime}
     */
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime lastChangedAt;

    /**
     * 동기화 되었음을 기록
     *
     * @param time {@code TourApi} 의 {@code modifiedtime}
     */
    public void synchronizeTo(LocalDateTime time) {
        this.lastChangedAt = time;
        this.lastModifiedAt = LocalDateTime.now();
    }
}
