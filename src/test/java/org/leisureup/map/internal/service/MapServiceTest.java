package org.leisureup.map.internal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.leisureup.map.internal.dto.KakaoPlaceResponse;
import org.leisureup.map.internal.dto.KakaoPlaceResponse.Document;
import org.leisureup.map.internal.dto.KakaoPlaceResponse.Meta;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MapServiceTest {
    @Mock
    private PlaceSearchService placeSearchService;

    @InjectMocks
    private MapService mapService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void searchCategory_ReturnsResponseFromPlaceSearchService() {
        // given
        double x = 127.0;
        double y = 37.5;
        int radius = 1000;
        String category = "hospital";
        String kakaoCategory = "HP8"; // Category.toKakaoCategory(category) 결과
        KakaoPlaceResponse mockResponse = createMockResponse();
        when(placeSearchService.searchPharmacies(x, y, radius, kakaoCategory)).thenReturn(mockResponse);

        // when
        KakaoPlaceResponse result = mapService.searchCategory(x, y, radius, category);

        // then
        assertResponse(result);
        verify(placeSearchService, times(1)).searchPharmacies(x, y, radius, kakaoCategory);
    }

    private KakaoPlaceResponse createMockResponse() {
        KakaoPlaceResponse response = new KakaoPlaceResponse();
        Meta meta = new Meta();
        setField(meta, "total_count", 1);
        setField(meta, "pageable_count", 1);
        setField(meta, "is_end", true);

        Document doc = new Document();
        setField(doc, "place_name", "테스트약국");
        setField(response, "meta", meta);
        setField(response, "documents", Collections.singletonList(doc));
        return response;
    }

    private void assertResponse(KakaoPlaceResponse result) {
        assertThat(result).isNotNull();
        assertThat(result.getDocuments()).hasSize(1);
        assertThat(result.getDocuments().get(0).getPlace_name()).isEqualTo("테스트약국");
    }

    // util method: reflection으로 private 필드 세팅
    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}