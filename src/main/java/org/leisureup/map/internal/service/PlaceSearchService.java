package org.leisureup.map.internal.service;

import lombok.RequiredArgsConstructor;
import org.leisureup.map.internal.dto.KakaoPlaceResponse;
import org.leisureup.map.internal.dto.KakaoRegionResponse;
import org.leisureup.map.internal.dto.TourApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceSearchService {
    private final KakaoLocalClient kakaoLocalClient;
    private final TourApiLocalClient tourApiLocalClient;

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    @Value("${tourApi.types.app}")
    private String mobileApp;
    @Value("${tourApi.key}")
    private String serviceKey;
    @Value("${tourApi.types.response}")
    private String _type;
    @Value("${tourApi.types.os}")
    private String mobileOS;

    public KakaoPlaceResponse searchPharmacies(double longitude, double latitude, int radiusMeters,
                                               String category, int pageNo, int pageSize) {
        String authorization = "KakaoAK " + kakaoApiKey;
        return kakaoLocalClient.searchByCategory(
                authorization,
                category, // 약국
                String.valueOf(longitude),
                String.valueOf(latitude),
                radiusMeters,
                "distance",
                pageNo,
                pageSize
        );
    }

    public KakaoRegionResponse getRegionByCoord(double longitude, double latitude) {
        String authorization = "KakaoAK " + kakaoApiKey;
        try {
            return kakaoLocalClient.coordToRegion(
                    authorization,
                    String.valueOf(longitude),
                    String.valueOf(latitude),
                    "WGS84"
            );
        } catch (Exception ignored) {
            return null; // 서비스 영역 밖 등 오류 시 폴백을 위해 null 반환
        }
    }

    public TourApiResponse getTourApi(double x, double y, int radius, Integer contentTypeId, int pageNo, int pageSize) {
        return tourApiLocalClient.searchByCategory(
                mobileOS,
                mobileApp,
                serviceKey,
                x,
                y,
                radius,
                _type,
                "C",
                contentTypeId,
                pageSize, pageNo
        );
    }

    public TourApiResponse getLocationBySearch(String keyword, int pageNo, int pageSize) {
        return tourApiLocalClient.search(
                mobileOS, mobileApp, serviceKey, _type, keyword, pageSize, pageNo
        );
    }

    public TourApiResponse getLocationBySearchWithRegion(
            String keyword,
            Integer lDongRegnCd,
            Integer lDongSignguCd,
            int pageNo,
            int pageSize
    ) {
        return tourApiLocalClient.searchWithRegion(
                mobileOS,
                mobileApp,
                serviceKey,
                _type,
                keyword,
                lDongRegnCd,
                lDongSignguCd,
                pageSize,
                pageNo
        );
    }
}
