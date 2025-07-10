package org.leisureup.map.internal.dto.request;

import io.swagger.v3.oas.annotations.media.*;
import jakarta.validation.constraints.*;
import java.util.*;
import lombok.*;
import org.leisureup.global.validation.*;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchLeisureRequest {

    @NotNull(message = "x 좌표는 필수입니다.")
    private Double x;
    @NotNull(message = "y 좌표는 필수입니다.")
    private Double y;

    @Positive(message = "radius 는 0 보다 커야합니다.")
    @Max(value = 20_000, message = "radius 는 20,000 보다 작아야 합니다.")
    @Schema(defaultValue = "1000")
    private int radius = 1_000;

    @Size(max = 5, message = "필터링은 최대 5 개 까지 가능합니다.")
    private Set<LeisureFilter> filters;

    @Min(value = 1, message = "page 는 1 보다 커야합니다.")
    @Schema(defaultValue = "1")
    private int page = 1;
    @Min(value = 1, message = "size 는 1 보다 커야합니다.")
    @Schema(defaultValue = "10")
    private int size = 10;

    @ValidEnum(target = Sort.class, message = "sort 를 맵핑할 수 없습니다.")
    @Schema(defaultValue = "DIST")
    Sort sort = Sort.DIST;

    public enum Sort {
        DIST, TITLE, CREATION, MODIFICATION
    }

    public CordRelatedInfo getCordRelatedInfo() {
        return new CordRelatedInfo(x, y, radius);
    }

    public PagingInfo getPagingInfo() {
        return new PagingInfo(page, size, sort);
    }
}
