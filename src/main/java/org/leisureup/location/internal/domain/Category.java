package org.leisureup.location.internal.domain;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import java.time.*;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.*;

/**
 * 카테고리 타입 분류
 */
@Getter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "category_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public abstract class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(length = 50, nullable = false)
    private String name;

    @NotBlank
    @Column(length = 20, nullable = false, unique = true)
    private String categoryCode;

    @Embedded
    private CategoryRecommend recommendation;

    @Embedded
    private AdditionalCategoryInfo additionalInfo;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime lastModifiedAt;

    protected Category(String name, String categoryCode) {
        this.name = name;
        this.categoryCode = categoryCode;
        this.recommendation = CategoryRecommend.of("");
    }

    protected Category(String name, String categoryCode, String recommendationCode) {
        this.name = name;
        this.categoryCode = categoryCode;
        this.recommendation = CategoryRecommend.of(recommendationCode);
    }

    protected Category(
            String name, String categoryCode, String recommendationCode,
            AdditionalCategoryInfo additionalInfo
    ) {
        this(name, categoryCode, recommendationCode);
        this.additionalInfo = AdditionalCategoryInfo.of(additionalInfo);
    }

    public abstract String getCategoryType();
}
