package org.leisureup.info.weather.service.client;

import java.util.*;
import java.util.concurrent.*;
import org.leisureup.global.response.external.*;
import org.leisureup.info.weather.dto.*;
import org.leisureup.info.weather.dto.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;

@Component
public class ShortTermForecastApiClient {

    private final String key, rspType;
    private final ShortTermForecastApi shortTermForecastApi;

    public ShortTermForecastApiClient(
            ShortTermForecastApi shortTermForecastApi,
            @Value("${weatherApi.forecast.short-term.key}") String key,
            @Value("${weatherApi.forecast.short-term.type}") String rspType
    ) {
        this.shortTermForecastApi = shortTermForecastApi;
        this.key = key;
        this.rspType = rspType;
    }

    /**
     * 주어진 정보 {@code (nx, ny, dateTimeInfo)} 로 얼만큼의 요청을 보내야 할지 알려준다.
     */
    public List<PagingRequestPlan> inspectPagingPlan(
            int nx, int ny, ShortTermBaseDateTimeInfo dateTimeInfo
    ) {
        var resp = shortTermForecastApi.getShortTermForecast(
                key, rspType,
                dateTimeInfo.baseDate(), dateTimeInfo.baseTime(),
                nx, ny, 1, 1
        );

        ShortTermForecastApiClientUtils.validateResp(resp);

        int total = resp.getResponse().body().totalCount();

        return ShortTermForecastApiClientUtils.buildPagingPlan(total);
    }

    /**
     * 주어진 정보로 단기 예보를 조회한다.
     * <p>
     * 단기 예보 데이터 개수가 {@code 1000} 개 정도 되는데 너무 느려서 {@code (페이징 + 비동기)} 방식으로 만듬.
     */
    @Async
    public CompletableFuture<List<ShortTermForecast>> getShortTermForecast(
            int nx, int ny, ShortTermBaseDateTimeInfo dateTimeInfo,
            PagingRequestPlan pagingReq
    ) {

        int pageNo = pagingReq.pageNo();
        int pageSize = pagingReq.pageSize();

        var resp = shortTermForecastApi.getShortTermForecast(
                key, rspType,
                dateTimeInfo.baseDate(), dateTimeInfo.baseTime(),
                nx, ny, pageNo, pageSize
        );

        ShortTermForecastApiClientUtils.validateResp(resp);

        return CompletableFuture.completedFuture(resp.getItems());
    }
}


class ShortTermForecastApiClientUtils {

    private static final int DEFAULT_PAGING_SIZE = 200;

    static void validateResp(ExternalApiResponse<?> resp) {
        ForecastUtils.validateResp(resp);
    }

    static List<PagingRequestPlan> buildPagingPlan(int totalCnt) {

        if (totalCnt <= 0) {
            throw new IllegalArgumentException("Total count must be greater than zero");
        }

        if (totalCnt <= DEFAULT_PAGING_SIZE) {
            return List.of(PagingRequestPlan.of(1, totalCnt));
        }

        int totalPages = Math.ceilDiv(totalCnt, DEFAULT_PAGING_SIZE);
        List<PagingRequestPlan> result = new ArrayList<>(totalPages);

        int remains = totalCnt;
        for (int pageNo = 1; pageNo <= totalPages; pageNo++) {

            int pageSize = Math.min(
                    remains,
                    DEFAULT_PAGING_SIZE
            );

            result.add(PagingRequestPlan.of(pageNo, pageSize));

            // 이전에 전체 page 가 1 인 경우 처리했기 때문에
            // 루프 속 remains 는 항상 0 보다 큼이 보장된다.
            remains -= DEFAULT_PAGING_SIZE;
        }

        return result;
    }
}
