package org.leisureup.info.weather.dto;

public record PagingRequestPlan(
        int pageNo, int pageSize
) {

    public static PagingRequestPlan of(int pageNo, int pageSize) {
        return new PagingRequestPlan(pageNo, pageSize);
    }
}
