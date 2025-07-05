package org.leisureup.map.internal.service;

import lombok.RequiredArgsConstructor;
import org.leisureup.global.exception.NotFound;
import org.leisureup.map.internal.domain.Category;
import org.leisureup.map.internal.dto.KakaoPlaceResponse;
import org.leisureup.map.internal.dto.TourApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MapService {
    private final PlaceSearchService placeSearchService;
    private final TourApiLocalClient tourApiLocalClient;

    public Object searchCategory(double x, double y, int radius, String category) {
        if (category.equals("hospital")) {
            String kakaoCategory = Category.toKakaoCategory(category);
            KakaoPlaceResponse kakaoPlaceResponse = placeSearchService.searchPharmacies(x, y, radius, kakaoCategory);
            return kakaoPlaceResponse;
        } else {
            // Category enum에서 contentTypeId 가져오기
            Integer contentTypeId = Category.valueOf(category).getTourContentTypeId();
            if (contentTypeId == null) {
                throw new NotFound("지원하지 않는 카테고리입니다: " + category);
            }
            System.out.println("쿼리가 나가긴함");
            TourApiResponse tourApi = placeSearchService.getTourApi(x, y, radius, contentTypeId);

            // 데이터가 없으면 404 에러 발생
            TourApiResponse.Body body = tourApi.getResponse().getBody();
            if (body == null || body.getItemsAsObject() == null ||
                body.getItemsAsObject().getItem() == null ||
                body.getItemsAsObject().getItem().isEmpty()) {
                throw new NotFound("해당 조건에 맞는 관광정보가 없습니다.");
            }
            return tourApi;
        }
    }
}
