package org.leisureup.map.internal.service;

import lombok.RequiredArgsConstructor;
import org.leisureup.global.exception.NotFound;
import org.leisureup.map.internal.domain.Category;
import org.leisureup.map.internal.dto.KakaoPlaceResponse;
import org.leisureup.map.internal.dto.MapResponse;
import org.leisureup.map.internal.dto.TourApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MapService {
    private final PlaceSearchService placeSearchService;

    public List<MapResponse> searchCategory(double x, double y, int radius, String category) {
        if (category.equals("hospital")) {
            String kakaoCategory = Category.toKakaoCategory(category);
            KakaoPlaceResponse kakaoPlaceResponse = placeSearchService.searchPharmacies(x, y, radius, kakaoCategory);
            return MapResponse.fromKakaoPlace(kakaoPlaceResponse);
        } else {
            Integer contentTypeId = Category.valueOf(category).getTourContentTypeId();
            if (contentTypeId == null) {
                throw new NotFound("지원하지 않는 카테고리입니다: " + category);
            }
            
            try {
                TourApiResponse tourApi = placeSearchService.getTourApi(x, y, radius, contentTypeId);
                TourApiResponse.Body body = tourApi.getResponse().getBody();
                
                // 데이터가 없으면 404 에러 발생
                if (body == null || body.getItems() == null || 
                    body.getItems().getItem() == null || 
                    body.getItems().getItem().isEmpty()) {
                    throw new NotFound("해당 조건에 맞는 관광정보가 없습니다.");
                }
                return MapResponse.fromTourAPI(tourApi,category);
            } catch (Exception e) {
                throw new NotFound("해당 조건에 맞는 관광정보가 없습니다.");
            }
        }
    }
}
