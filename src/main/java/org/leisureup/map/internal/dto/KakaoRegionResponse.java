package org.leisureup.map.internal.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class KakaoRegionResponse {
    private List<Document> documents;

    @Getter
    public static class Document {
        private String region_type;   // H(행정동)/B(법정동)
        private String address_name;  // 전체 지역 이름
        private String region_1depth_name; // 시도
        private String region_2depth_name; // 시군구
        private String region_3depth_name; // 읍면동
        private String region_4depth_name; // 리
        private String code;          // 법정동 코드(10자리)
        private double x;             // 중심 좌표 경도
        private double y;             // 중심 좌표 위도
    }
}


