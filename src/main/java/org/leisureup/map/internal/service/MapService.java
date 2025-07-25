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

    public MultiPageResponse<MapResponse> searchCategory(double x, double y, int radius, String category, int pageNo, int pageSize) {
        List<MapResponse> all;
        if (category.equals("hospital")) {
            String kakaoCategory = Category.toKakaoCategory(category);
            KakaoPlaceResponse kakaoPlaceResponse = placeSearchService.searchPharmacies(x, y, radius, kakaoCategory, pageNo, pageSize);
            all = MapResponse.fromKakaoPlace(kakaoPlaceResponse);
        } else {
            Integer contentTypeId = Category.valueOf(category).getTourContentTypeId();
            if (contentTypeId == null) {
                throw new NotFound("지원하지 않는 카테고리입니다: " + category);
            }
            try {
                TourApiResponse tourApi = placeSearchService.getTourApi(x, y, radius, contentTypeId, pageNo, pageSize);
                all = MapResponse.fromTourAPI(tourApi,category);
            } catch (Exception e) {
                throw new NotFound("해당 조건에 맞는 관광정보가 없습니다.");
            }
        }
        int total = all.size();
        int from = Math.max(0, (pageNo - 1) * pageSize);
        int to = Math.min(from + pageSize, total);
        List<MapResponse> page = all.subList(from, to);
        PageResponse<MapResponse> pageResp = PageResponse.of(pageNo, pageSize, total, page);
        Map<String, PageResponse<MapResponse>> map = Map.of("default", pageResp);
        return MultiPageResponse.of(pageNo, pageSize, map);
    }

    public MultiPageResponse<MapResponse> search(String query, int pageNo, int pageSize) {
        List<MapResponse> all;
        try{
            TourApiResponse locationBySearch = placeSearchService.getLocationBySearch(query, pageNo, pageSize);
            all = MapResponse.fromTourAPI(locationBySearch, query);
        } catch (Exception e) {
            throw new NotFound("검색 결과가 없습니다.");
        }
        int total = all.size();
        int from = Math.max(0, (pageNo - 1) * pageSize);
        int to = Math.min(from + pageSize, total);
        List<MapResponse> page = all.subList(from, to);
        PageResponse<MapResponse> pageResp = PageResponse.of(pageNo, pageSize, total, page);
        Map<String, PageResponse<MapResponse>> map = Map.of("default", pageResp);
        return MultiPageResponse.of(pageNo, pageSize, map);
    }
}
