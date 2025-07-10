package org.leisureup.map.internal.service;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import org.leisureup.global.exception.*;
import org.leisureup.global.response.external.tourapi.*;
import org.leisureup.global.response.external.tourapi.TourApiResponse;
import org.leisureup.map.internal.dto.*;
import org.leisureup.map.internal.dto.api.*;
import org.leisureup.map.internal.dto.request.*;
import org.leisureup.map.internal.dto.response.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;

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

    public PageResponse<SearchLeisureResponse> searchAnyLeisure(
            CordInfo cordInfo, PagingInfo pagingInfo
    ) {

        var reqDto = TourApiSearchServiceUtils.buildReqAdapterDto(cordInfo, pagingInfo);
        var resp = this.sendSearchRequest(reqDto);

        if (resp == null || !resp.isSuccess()) {
            throw TourApiSearchServiceUtils.buildExMsg(resp);
        }

        return TourApiSearchServiceUtils.buildPageResponseOn(
                pagingInfo.pageSize(), resp,
                SearchLeisureResponse::of
        );
    }

    @Async
    public CompletableFuture<PageResponse<SearchLeisureResponse>> searchLeisure(
            CordInfo cordInfo, PagingInfo pagingInfo,
            LeisureFilter filter
    ) {

        var reqDto = TourApiSearchServiceUtils.buildReqAdapterDto(cordInfo, pagingInfo, filter);
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

        return searchClient.searchOnLocationBase(
                key, app, os, rspType,
                x, y, radius,
                cat1, cat2, cat3,
                pageNo, pageSize, arrange
        );
    }
}

class TourApiSearchServiceUtils {

    static LocationBaseSearchAdapterDto buildReqAdapterDto(
            CordInfo cordInfo, PagingInfo pagingInfo
    ) {
        return new LocationBaseSearchAdapterDto(cordInfo, pagingInfo);
    }

    static LocationBaseSearchAdapterDto buildReqAdapterDto(
            CordInfo cordInfo, PagingInfo pagingInfo, LeisureFilter filter
    ) {
        if (filter == null) {
            throw new IllegalArgumentException("LeisureFilter cannot be null");
        }

        var dto = new LocationBaseSearchAdapterDto(cordInfo, pagingInfo);
        dto.setCategory(filter.getFullCode());

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
        Body<I> body = apiResponse.response().body();

        int pageNo = body.pageNo();
        int numOfTotalElements = body.totalCount();
        List<R> mappedElements = body.getItems().stream()
                .map(respMapper)
                .toList();

        return PageResponse.of(pageNo, pageSize, numOfTotalElements, mappedElements);
    }
}

