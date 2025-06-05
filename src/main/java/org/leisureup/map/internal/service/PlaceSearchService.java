package org.leisureup.map.internal.service;

import lombok.RequiredArgsConstructor;
import org.leisureup.map.internal.dto.KakaoPlaceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceSearchService {
    private final KakaoLocalClient kakaoLocalClient;

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    public KakaoPlaceResponse searchPharmacies(double longitude, double latitude, int radiusMeters,
                                               String category) {
        String authorization = "KakaoAK " + kakaoApiKey;
        return kakaoLocalClient.searchByCategory(
                authorization,
                category, // 약국
                String.valueOf(longitude),
                String.valueOf(latitude),
                radiusMeters,
                "distance",
                1,
                10
        );
    }
}
