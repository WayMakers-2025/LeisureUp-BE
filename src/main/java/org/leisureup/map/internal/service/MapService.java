package org.leisureup.map.internal.service;

import lombok.RequiredArgsConstructor;
import org.leisureup.global.exception.NotFound;
import org.leisureup.map.internal.domain.Category;
import org.leisureup.map.internal.dto.KakaoPlaceResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MapService {
    private final PlaceSearchService placeSearchService;

    public KakaoPlaceResponse searchCategory(double x, double y,
                                             int radius,
                                             String category) {
        String kakaoCategory = Category.toKakaoCategory(category);
        return placeSearchService.searchPharmacies(x, y, radius, kakaoCategory);
    }
}
