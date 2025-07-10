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

@Service
@RequiredArgsConstructor
public class LeisureSearchService {

    public static final String FILTER_NAME_ON_ANY_SEARCH = "default";
    private final TourApiSearchService apiSearchService;

    public MultiPageResponse<SearchLeisureResponse> searchAnyLeisure(
            CordInfo cordInfo, PagingInfo pagingInfo
    ) {

        Map<String, PageResponse<SearchLeisureResponse>> resp = Map.of(
                FILTER_NAME_ON_ANY_SEARCH,
                apiSearchService.searchAnyLeisure(cordInfo, pagingInfo)
        );

        return MultiPageResponse.of(pagingInfo.pageNo(), pagingInfo.pageSize(), resp);
    }

    public MultiPageResponse<SearchLeisureResponse> searchLeisureWithFilters(
            CordInfo cordInfo, PagingInfo pagingInfo,
            Set<LeisureFilter> filters
    ) {

        Map<String, CompletableFuture<PageResponse<SearchLeisureResponse>>> futureResponses
                = filters.stream().collect(Collectors.toMap(
                Enum::name,
                f -> apiSearchService.searchLeisure(cordInfo, pagingInfo, f)
        ));

        Map<String, PageResponse<SearchLeisureResponse>> joinedResponses
                = LeisureSearchServiceUtils.joinAllFutureResponses(futureResponses);

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
            all.join();

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