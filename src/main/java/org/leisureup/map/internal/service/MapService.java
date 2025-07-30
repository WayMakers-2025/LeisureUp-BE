package org.leisureup.map.internal.service;

import lombok.RequiredArgsConstructor;
import org.leisureup.global.exception.NotFound;
import org.leisureup.map.internal.domain.Category;
import org.leisureup.map.internal.dto.KakaoPlaceResponse;
import org.leisureup.map.internal.dto.MapResponse;
import org.leisureup.map.internal.dto.TourApiResponse;
import org.leisureup.map.internal.dto.response.MultiPageResponse;
import org.leisureup.map.internal.dto.response.PageResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MapService {
    private final PlaceSearchService placeSearchService;

    public PageResponse<MapResponse> searchCategory(double x, double y, int radius, Category category, int pageNo, int pageSize) {
        List<MapResponse> elements;
        int totalCount;
        
        if (category == Category.hospital) {
            String kakaoCategory = category.getKakaoCategory();
            KakaoPlaceResponse kakaoPlaceResponse = placeSearchService.searchPharmacies(x, y, radius, kakaoCategory, pageNo, pageSize);
            elements = MapResponse.fromKakaoPlace(kakaoPlaceResponse);
            totalCount = kakaoPlaceResponse.getMeta().getTotal_count();
        } else {
            Integer contentTypeId = category.getTourContentTypeId();
            if (contentTypeId == null) {
                throw new NotFound("지원하지 않는 카테고리입니다: " + category);
            }
            
            TourApiResponse tourApi = placeSearchService.getTourApi(x, y, radius, contentTypeId, pageNo, pageSize);
            elements = MapResponse.fromTourAPI(tourApi, category.name());
            totalCount = tourApi.getResponse().getBody().getTotalCount();
        }
        
        return PageResponse.of(pageNo, pageSize, totalCount, elements);
    }

    public PageResponse<MapResponse> search(String query, int pageNo, int pageSize) {
        List<MapResponse> elements;
        int totalCount;
        
        TourApiResponse locationBySearch = placeSearchService.getLocationBySearch(query, pageNo, pageSize);
        elements = MapResponse.fromTourAPI(locationBySearch, query);
        totalCount = locationBySearch.getResponse().getBody().getTotalCount();
        
        return PageResponse.of(pageNo, pageSize, totalCount, elements);
    }
}
