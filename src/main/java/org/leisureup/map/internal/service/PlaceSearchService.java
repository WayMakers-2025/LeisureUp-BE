package org.leisureup.map.internal.service;

import lombok.RequiredArgsConstructor;
import org.leisureup.map.internal.dto.KakaoPlaceResponse;
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

    public TourApiResponse getTourApi(double x, double y, int radius, Integer contentTypeId) {
        System.out.println("real 로?");
        
        // URL 구성해서 출력
        String url = String.format(
            "http://apis.data.go.kr/B551011/KorService2/locationBasedList2?" +
            "MobileOS=%s&MobileApp=%s&serviceKey=%s&mapX=%f&mapY=%f&radius=%d" +
            "&_type=%s&arrange=C&contentTypeId=%d&numOfRows=10&pageNo=1",
            mobileOS, mobileApp, serviceKey, x, y, radius, _type, contentTypeId
        );
        System.out.println("TourAPI URL: " + url);
        
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
                10,1
        );
    }
}
