package org.leisureup.map.internal.service;

import lombok.RequiredArgsConstructor;
import org.leisureup.global.exception.NotFound;
import org.leisureup.map.internal.domain.Category;
import org.leisureup.map.internal.dto.KakaoPlaceResponse;
import org.leisureup.map.internal.dto.KakaoRegionResponse;
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

    public PageResponse<MapResponse> search(double x, double y, String query, int pageNo, int pageSize) {
        KakaoRegionResponse regionResp = placeSearchService.getRegionByCoord(x, y);
        Integer lDongRegnCd = null;     // 법정동 시도 코드 (앞 2자리)
        Integer lDongSignguCd = null;   // 법정동 시군구 코드 (3~5번째)

        if (regionResp != null && regionResp.getDocuments() != null && !regionResp.getDocuments().isEmpty()) {
            String code10 = regionResp.getDocuments().stream()
                    .filter(doc -> "B".equalsIgnoreCase(doc.getRegion_type()))
                    .map(KakaoRegionResponse.Document::getCode)
                    .findFirst()
                    .orElseGet(() -> regionResp.getDocuments().get(0).getCode());

            if (code10 != null && code10.length() >= 5) {
                try {
                    // 앞 2자리 = 시도, 이후 3자리 = 시군구
                    lDongRegnCd = Integer.parseInt(code10.substring(0, 2));
                    lDongSignguCd = Integer.parseInt(code10.substring(2, 5));
                } catch (NumberFormatException ignored) {
                    lDongRegnCd = null;
                    lDongSignguCd = null;
                }
            }
        }

        TourApiResponse locationBySearch = (lDongRegnCd != null && lDongSignguCd != null)
                ? placeSearchService.getLocationBySearchWithRegion(
                        query,
                        lDongRegnCd,
                        lDongSignguCd,
                        pageNo,
                        pageSize
                )
                : placeSearchService.getLocationBySearch(query, pageNo, pageSize);

        List<MapResponse> elements = MapResponse.fromTourAPI(locationBySearch, query);
        int totalCount = locationBySearch.getResponse().getBody().getTotalCount();

        return PageResponse.of(pageNo, pageSize, totalCount, elements);
    }
}
