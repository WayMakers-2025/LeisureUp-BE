package org.leisureup.map.internal.service;

import java.util.*;
import java.util.Map.*;
import java.util.concurrent.*;
import java.util.stream.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.leisureup.global.exception.*;
import org.leisureup.map.internal.dto.request.*;
import org.leisureup.map.internal.dto.response.*;
import org.springframework.stereotype.*;

/**
 * 레저 장소 검색을 위한 {@code service}
 */
@Service
@RequiredArgsConstructor
public class LeisureSearchService {

    public static final String FILTER_NAME_ON_ANY_SEARCH = "default";
    private final TourApiSearchService apiSearchService;

    /**
     * 레저 종류 필터링 없이 기본 검색을 진행
     */
    public MultiPageResponse<SearchLeisureResponse> searchAnyLeisure(
            CordInfo cordInfo, PagingInfo pagingInfo
    ) {

        // 아무 필터 없이 장소를 검색 (비동기 X)
        Map<String, PageResponse<SearchLeisureResponse>> resp = Map.of(
                FILTER_NAME_ON_ANY_SEARCH,
                apiSearchService.searchAnyLeisure(cordInfo, pagingInfo)
        );

        // 페이징 결과로 반환, return
        return MultiPageResponse.of(pagingInfo.pageNo(), pagingInfo.pageSize(), resp);
    }

    /**
     * 주어진 레저 필터에 해당하는 레저 장소를 검색
     */
    public MultiPageResponse<SearchLeisureResponse> searchLeisureWithFilters(
            CordInfo cordInfo, PagingInfo pagingInfo,
            Set<LeisureFilter> filters
    ) {

        // 주어진 레저 필터별 비동기 API 호출 진행
        Map<String, CompletableFuture<PageResponse<SearchLeisureResponse>>> futureResponses
                = filters.stream().collect(Collectors.toMap(
                Enum::name,
                f -> apiSearchService.searchLeisure(cordInfo, pagingInfo, f)
        ));

        // 비동기로 호출 API 결과를 조합
        // 어느 한 API 라도 실패시 exception 반환
        Map<String, PageResponse<SearchLeisureResponse>> joinedResponses
                = LeisureSearchServiceUtils.joinAllFutureResponses(futureResponses);

        // 페이징 결과로 반환, return
        return MultiPageResponse.of(
                pagingInfo.pageNo(), pagingInfo.pageSize(),
                joinedResponses
        );
    }
}

@Slf4j
class LeisureSearchServiceUtils {

    static <T> Map<String, T> joinAllFutureResponses(
            Map<String, CompletableFuture<T>> futureResponses
    ) {

        CompletableFuture<Void> all = CompletableFuture.allOf(
                futureResponses.values().toArray(new CompletableFuture[0])
        );

        try {
            // 모든 task 가 마치길 기다림
            all.join();

            // CompletableFuture unwrap 해서 반환
            return Stream.of(futureResponses.entrySet())
                    .flatMap(Collection::stream)
                    .collect(Collectors.toMap(
                            Entry::getKey,
                            es -> es.getValue().join()
                    ));
        } catch (CompletionException e) {
            var cause = e.getCause();
            if (cause instanceof CustomException ce) {
                throw ce;
            }

            log.error(
                    "Unexpected [CompletionException] occurred caused by [{}]",
                    cause.getClass().getSimpleName(),
                    e
            );

            throw e;
        } catch (Exception e) {
            log.error(
                    "Unexpected exception [{}] occurred", e.getClass().getSimpleName(), e
            );

            throw e;
        }
    }
}