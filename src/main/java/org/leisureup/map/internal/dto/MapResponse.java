package org.leisureup.map.internal.dto;

import java.util.*;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MapResponse {
    // 영업시간, 예약 가능 필드 없음!!!!
    private Long contentId;
    private String category;
    private String addr1; // 주소
    private String addr2; // 상세주소
    private String firstImage; // 대표이미지(원본)
    private String firstImage2; // 대표이미지(썸네일)
    private String mapX; // GPS X좌표
    private String mapY; // GPS Y좌표
    private String tel; // 전화번호
    private String title; // 제목

    public static List<MapResponse> fromKakaoPlace(KakaoPlaceResponse kakaoPlaceResponse) {
        List<MapResponse> mapResponses = new ArrayList<>();
        for (KakaoPlaceResponse.Document document : kakaoPlaceResponse.getDocuments()) {
            MapResponse build = MapResponse.builder()
                    .category("hospital")
                    .addr1(document.getAddress_name())
                    .firstImage(document.getPlace_url())
                    .mapX(document.getX())
                    .mapY(document.getY())
                    .tel(document.getPhone())
                    .title(document.getPlace_name())
                    .build();
            mapResponses.add(build);
        }
        return mapResponses;
    }

    public static List<MapResponse> fromTourAPI(TourApiResponse tourApiResponse, String category) {
        List<MapResponse> mapResponses = new ArrayList<>();
        for (TourApiResponse.Item item : tourApiResponse.getResponse().getBody().getItems().getItem()) {
            MapResponse build = MapResponse.builder()
                    .contentId(item.getContentid())
                    .category(category)
                    .addr1(item.getAddr1())
                    .addr2(item.getAddr2())
                    .firstImage(item.getFirstimage())
                    .firstImage2(item.getFirstimage2())
                    .mapX(item.getMapx())
                    .mapY(item.getMapy())
                    .tel(item.getTel())
                    .title(item.getTitle())
                    .build();
            mapResponses.add(build);
        }
        return mapResponses;
    }
}
