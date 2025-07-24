package org.leisureup.travel.internal.travel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherApiResponse {
    private Header header;
    private Body body;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Body {
        private String dataType;
        private Items items;
        private int numOfRows;
        private int pageNo;
        private int totalCount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Items {
        private List<Item> item;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        private String regId;
        private String rnSt4Am; // 4일 후 오전 강수 확률
        private String rnSt4Pm; // 4일 후 오후 강수 확률
        private String rnSt5Am;
        private String rnSt5Pm;
        private String rnSt6Am;
        private String rnSt6Pm;
        private String rnSt7Am;
        private String rnSt7Pm;
        private String rnSt8;
        private String rnSt9;
        private String rnSt10;
        private String wf4Am; // 4일 후 오전 날씨예보
        private String wf4Pm; // 4일 후 오후 날씨예보
        private String wf5Am;
        private String wf5Pm;
        private String wf6Am;
        private String wf6Pm;
        private String wf7Am;
        private String wf7Pm;
        private String wf8;
        private String wf9;
        private String wf10;
    }
} 