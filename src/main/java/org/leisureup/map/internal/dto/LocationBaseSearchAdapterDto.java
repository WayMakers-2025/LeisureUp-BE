package org.leisureup.map.internal.dto;

import lombok.*;
import org.leisureup.map.internal.dto.request.*;
import org.leisureup.map.internal.dto.request.SearchLeisureRequest.*;
import org.leisureup.map.internal.service.*;

/**
 * {@link TourApiSearchService} 에서 요청과 관련된 정보를 교환하기 위한 Util DTO
 */
@Getter
public class LocationBaseSearchAdapterDto {

    private final double x, y;
    private final int radius, pageNo, pageSize;
    private String cat1 = "A03";
    private String cat2, cat3, arrange;

    public LocationBaseSearchAdapterDto(
            double x, double y, int radius, int pageNo, int pageSize
    ) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public LocationBaseSearchAdapterDto(
            CordInfo cordInfo, PagingInfo pagingInfo
    ) {
        this.x = cordInfo.x();
        this.y = cordInfo.y();
        this.radius = cordInfo.radius();

        this.pageNo = pagingInfo.pageNo();
        this.pageSize = pagingInfo.pageSize();
        this.arrange = resolveSorting(pagingInfo.sorting());
    }

    private static String resolveSorting(Sort sorting) {
        return switch (sorting) {
            case TITLE -> Arrange.TITLE.arrange;
            case MODIFICATION -> Arrange.MODIFICATION.arrange;
            case CREATION -> Arrange.CREATION.arrange;
            default -> Arrange.DIST.arrange;
        };
    }

    public void setCategory(String fullCat) {

        if (fullCat == null || fullCat.length() != 9) {
            throw new IllegalArgumentException("Invalid category");
        }

        this.cat1 = fullCat.substring(0, 3);
        this.cat2 = fullCat.substring(0, 5);
        this.cat3 = fullCat;
    }

    public void sortWithTitle() {
        this.arrange = Arrange.TITLE.arrange;
    }

    public void sortWithModification() {
        this.arrange = Arrange.MODIFICATION.arrange;
    }

    public void sortWithCreation() {
        this.arrange = Arrange.CREATION.arrange;
    }

    public void sortWithDist() {
        this.arrange = Arrange.DIST.arrange;
    }

    @Getter
    @RequiredArgsConstructor
    private enum Arrange {
        TITLE("A"), MODIFICATION("C"),
        CREATION("D"), DIST("E");
        private final String arrange;
    }
}
