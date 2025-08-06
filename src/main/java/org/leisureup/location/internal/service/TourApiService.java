package org.leisureup.location.internal.service;

import feign.*;
import lombok.extern.slf4j.*;
import org.leisureup.global.exception.*;
import org.leisureup.global.response.external.*;
import org.leisureup.global.response.external.tourapi.*;
import org.leisureup.location.internal.dto.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Slf4j
@Service
public class TourApiService {

    private final TourApiClient apiClient;

    private final String key, app, os;
    private final String rspType;

    public TourApiService(
            TourApiClient apiClient,
            @Value("${tourApi.key}") String apiKey,
            @Value("${tourApi.types.app}") String appType,
            @Value("${tourApi.types.os}") String osType,
            @Value("${tourApi.types.response}") String responseType
    ) {
        this.apiClient = apiClient;
        this.key = apiKey;
        this.app = appType;
        this.os = osType;
        this.rspType = responseType;
    }

    private static TourApiException buildExMsg(ExternalApiResponse<?> response) {
        String message = "API 통신 중 에러가 발생했습니다.";

        try {
            message += " : " + response.getResultMessage();
        } catch (Exception ignored) {
            // 응답 메시지까지 못받았으면 그냥 반환한다.
        }

        return new TourApiException(message);
    }

    /**
     * {@code locationId} 에 해당하는 정보를 API 로 가져온다.
     * <p>
     * 정보 fetch 에 실패하면 {@link TourApiException} 을 발생하고, 정보가 존재하지 않으면 {@link NotFound} 를 발생시킨다.
     */
    public CommonInfo getCommonInfo(Long locationId) {

        TourApiResponse<CommonInfo> resp;

        try {
            resp = apiClient.getCommonInfo(
                    locationId, key, app, os, rspType
            );
        } catch (RetryableException e) {
            log.warn("Failed to retrieve response", e);
            throw new ServerSideException(503, "API 통신에 실패했습니다.");
        }

        if (resp == null || !resp.isSuccess()) {
            throw buildExMsg(resp);
        }

        if (resp.isEmpty()) {
            throw new NotFound("Not found");
        }

        return resp.getSingleItem();
    }
}
