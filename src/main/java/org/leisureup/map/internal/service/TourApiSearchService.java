package org.leisureup.map.internal.service;

import feign.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import lombok.extern.slf4j.*;
import org.leisureup.global.exception.*;
import org.leisureup.global.response.external.base.Body;
import org.leisureup.global.response.external.tourapi.TourApiResponse;
import org.leisureup.map.internal.dto.*;
import org.leisureup.map.internal.dto.api.*;
import org.leisureup.map.internal.dto.request.*;
import org.leisureup.map.internal.dto.response.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;

@Slf4j
@Service
public class TourApiSearchService {

    private final TourApiSearchClient searchClient;

    private final String key, app, os;
    private final String rspType;

    public TourApiSearchService(
            TourApiSearchClient searchClient,
            @Value("${tourApi.key}") String apiKey,
            @Value("${tourApi.types.app}") String appType,
            @Value("${tourApi.types.os}") String osType,
            @Value("${tourApi.types.response}") String responseType
    ) {
        this.searchClient = searchClient;
        this.key = apiKey;
        this.app = appType;
        this.os = osType;
        this.rspType = responseType;
    }

    /**
     * 기본 검색 진행
     */
    public PageResponse<SearchLeisureResponse> searchAnyLeisure(
            CordInfo cordInfo, PagingInfo pagingInfo
    ) {

        // 요청에서 adapter DTO 구성 & 요청
        var reqDto = TourApiSearchServiceUtils.buildReqAdapterDto(cordInfo, pagingInfo);
        var resp = this.sendSearchRequest(reqDto);

        if (resp == null || !resp.isSuccess()) {
            throw TourApiSearchServiceUtils.buildExMsg(resp);
        }

        // TourApi 형식 결과를 SearchLeisureResponse 로 mapping 및 페이징 반환
        return TourApiSearchServiceUtils.buildPageResponseOn(
                pagingInfo.pageSize(), resp,
                SearchLeisureResponse::of
        );
    }

    /**
     * 레저 필터 검색 진행
     * <p>
     * {@link #searchAnyLeisure} 와 로직은 동일
     */
    @Async
    public CompletableFuture<PageResponse<SearchLeisureResponse>> searchLeisure(
            CordInfo cordInfo, PagingInfo pagingInfo,
            String categoryCode
    ) {

        var reqDto = TourApiSearchServiceUtils.buildReqAdapterDto(
                cordInfo, pagingInfo, categoryCode
        );
        var resp = this.sendSearchRequest(reqDto);

        if (resp == null || !resp.isSuccess()) {
            throw TourApiSearchServiceUtils.buildExMsg(resp);
        }

        return CompletableFuture.completedFuture(
                TourApiSearchServiceUtils.buildPageResponseOn(
                        pagingInfo.pageSize(), resp,
                        SearchLeisureResponse::of
                )
        );
    }

    private TourApiResponse<LocationBaseLeisureSearch> sendSearchRequest(
            LocationBaseSearchAdapterDto reqDto
    ) {
        double x = reqDto.getX();
        double y = reqDto.getY();
        int radius = reqDto.getRadius();

        String cat1 = reqDto.getCat1();
        String cat2 = reqDto.getCat2();
        String cat3 = reqDto.getCat3();

        int pageNo = reqDto.getPageNo();
        int pageSize = reqDto.getPageSize();
        String arrange = reqDto.getArrange();

        try {
            return searchClient.searchOnLocationBase(
                    key, app, os, rspType,
                    x, y, radius,
                    cat1, cat2, cat3,
                    pageNo, pageSize, arrange
            );
        } catch (RetryableException e) {
            log.warn("Failed to retrieve response", e);
            throw new ServerSideException(503, "API 통신에 실패했습니다.");
        }
    }
}

class TourApiSearchServiceUtils {

    static LocationBaseSearchAdapterDto buildReqAdapterDto(
            CordInfo cordInfo, PagingInfo pagingInfo
    ) {
        return new LocationBaseSearchAdapterDto(cordInfo, pagingInfo);
    }

    static LocationBaseSearchAdapterDto buildReqAdapterDto(
            CordInfo cordInfo, PagingInfo pagingInfo, String categoryCode
    ) {
        if (categoryCode == null || categoryCode.isEmpty()) {
            throw new IllegalArgumentException("Category code cannot be null or empty");
        }

        var dto = new LocationBaseSearchAdapterDto(cordInfo, pagingInfo);
        dto.setCategory(categoryCode);

        return dto;
    }

    static TourApiException buildExMsg(TourApiResponse<?> response) {
        String message = "API 통신 중 에러가 발생했습니다.";

        try {
            message += " : " + response.getResultMessage();
        } catch (Exception ignored) {
            // 응답 메시지까지 못받았으면 그냥 반환한다.
        }

        return new TourApiException(message);
    }

    static <I, R> PageResponse<R> buildPageResponseOn(
            int pageSize, TourApiResponse<I> apiResponse,
            Function<I, R> respMapper
    ) {
        Body<I> body = apiResponse.getResponse().body();

        int pageNo = body.pageNo();
        int numOfTotalElements = body.totalCount();
        List<R> mappedElements = body.getItems().stream()
                .map(respMapper)
                .toList();

        return PageResponse.of(pageNo, pageSize, numOfTotalElements, mappedElements);
    }
}

