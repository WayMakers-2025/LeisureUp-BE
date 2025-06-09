package org.leisureup.map.internal.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class KakaoPlaceResponse {
    private Meta meta;
    private List<Document> documents;

    @Getter
    public static class Meta {
        private int total_count;
        private int pageable_count;
        private boolean is_end;
    }

    @Getter
    public static class Document {
        private String id;
        private String place_name;
        private String category_name;
        private String category_group_code;
        private String category_group_name;
        private String phone;
        private String address_name;
        private String road_address_name;
        private String x;
        private String y;
        private String place_url;
        private String distance;
    }
}