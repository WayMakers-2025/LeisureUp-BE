package org.leisureup.info.weather.service.client;

import feign.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.leisureup.global.exception.*;
import org.leisureup.global.response.external.*;
import org.leisureup.global.response.external.weather.*;
import org.leisureup.info.weather.dto.*;
import org.leisureup.info.weather.dto.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;

@Slf4j
@Component
public class ShortTermForecastApiClient {

    private final String key, rspType;
    private final ShortTermForecastApi shortTermForecastApi;

    private static final int DEFAULT_RETRY_CNT = 5;
    private static final long DEFAULT_RETRY_DELAY_MS = 50;

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
        var resp = this.doRequestWithRetry(nx, ny, dateTimeInfo, 1, 1);

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

        var resp = this.doRequestWithRetry(nx, ny, dateTimeInfo, pageNo, pageSize);

        ShortTermForecastApiClientUtils.validateResp(resp);

        return CompletableFuture.completedFuture(resp.getItems());
    }

    /**
     * 단기 예보 API 가 이상한 응답 {@code (NO_DATA)} 을 자주 보내서 구성한 재시도 로직
     *
     * @return 재시도 했음에도 올바른 데이터를 받지 못햇으면 {@code null}
     */
    @SneakyThrows
    private WeatherApiResponse<ShortTermForecast> doRequestWithRetry(
            int nx, int ny, ShortTermBaseDateTimeInfo dateTimeInfo,
            int pageNo, int pageSize
    ) {
        WeatherApiResponse<ShortTermForecast> resp = null;

        int attempts = 1;
        for (; attempts <= DEFAULT_RETRY_CNT; attempts++) {
            try {
                resp = shortTermForecastApi.getShortTermForecast(
                        key, rspType,
                        dateTimeInfo.baseDate(), dateTimeInfo.baseTime(),
                        nx, ny, pageNo, pageSize
                );
            } catch (RetryableException e) {
                log.warn("Failed to retrieve response", e);
                throw new ServerSideException(503, "API 통신에 실패했습니다.");
            }

            if (!ShortTermForecastApiClientUtils.shouldRetry(resp)) {
                break;
            }

            log.warn("Retrying api call with attempts: [{}]", attempts);
            resp = null;

            Thread.sleep(DEFAULT_RETRY_DELAY_MS);
        }

        if (resp == null) {
            log.warn("Failed to fetch data with attempts: [{}]", attempts - 1);
        }

        return resp;
    }
}


class ShortTermForecastApiClientUtils {

    private static final int SINGLE_PAGE_SIZE_THRESHOLD = 500;
    private static final int DEFAULT_PAGING_SIZE = 100;

    static boolean shouldRetry(ExternalApiResponse<?> unvalidatedResp) {

        if (unvalidatedResp == null) {
            return true;
        }

        String resultMsg = unvalidatedResp.getResultMessage();
        return "NO_DATA".equals(resultMsg);
    }

    static void validateResp(ExternalApiResponse<?> resp) {
        ForecastUtils.validateResp(resp);
    }

    static List<PagingRequestPlan> buildPagingPlan(int totalCnt) {

        if (totalCnt <= 0) {
            throw new IllegalArgumentException("Total count must be greater than zero");
        }

        if (totalCnt <= SINGLE_PAGE_SIZE_THRESHOLD) {
            return List.of(PagingRequestPlan.of(1, totalCnt));
        }

        int totalPages = Math.ceilDiv(totalCnt, DEFAULT_PAGING_SIZE);

        return IntStream.rangeClosed(1, totalPages)
                .mapToObj(
                        pno -> PagingRequestPlan.of(pno, DEFAULT_PAGING_SIZE)
                )
                .toList();
    }
}
