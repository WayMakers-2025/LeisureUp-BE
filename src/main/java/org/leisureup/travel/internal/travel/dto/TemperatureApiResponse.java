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
public class TemperatureApiResponse {
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
        private String regId;           // 예보구역코드
        
        // 4일 후 예상기온 (6시 발표의 경우만)
        private Integer taMin4;         // 4일 후 예상최저기온(℃)
        private Integer taMin4Low;      // 4일 후 예상최저기온 하한 범위
        private Integer taMin4High;     // 4일 후 예상최저기온 상한 범위
        private Integer taMax4;         // 4일 후 예상최고기온(℃)
        private Integer taMax4Low;      // 4일 후 예상최고기온 하한 범위
        private Integer taMax4High;     // 4일 후 예상최고기온 상한 범위
        
        // 5일 후 예상기온
        private Integer taMin5;         // 5일 후 예상최저기온(℃)
        private Integer taMin5Low;      // 5일 후 예상최저기온 하한 범위
        private Integer taMin5High;     // 5일 후 예상최저기온 상한 범위
        private Integer taMax5;         // 5일 후 예상최고기온(℃)
        private Integer taMax5Low;      // 5일 후 예상최고기온 하한 범위
        private Integer taMax5High;     // 5일 후 예상최고기온 상한 범위
        
        // 6일 후 예상기온
        private Integer taMin6;         // 6일 후 예상최저기온(℃)
        private Integer taMin6Low;      // 6일 후 예상최저기온 하한 범위
        private Integer taMin6High;     // 6일 후 예상최저기온 상한 범위
        private Integer taMax6;         // 6일 후 예상최고기온(℃)
        private Integer taMax6Low;      // 6일 후 예상최고기온 하한범위
        private Integer taMax6High;     // 6일 후 예상최고기온 상한범위
        
        // 7일 후 예상기온
        private Integer taMin7;         // 7일 후 예상최저기온(℃)
        private Integer taMin7Low;      // 7일 후 예상최저기온 하한범위
        private Integer taMin7High;     // 7일 후 예상최저기온 상한범위
        private Integer taMax7;         // 7일 후 예상최고기온(℃)
        private Integer taMax7Low;      // 7일 후 예상최고기온 하한범위
        private Integer taMax7High;     // 7일 후 예상최고기온 상한범위
        
        // 8일 후 예상기온
        private Integer taMin8;         // 8일 후 예상최저기온(℃)
        private Integer taMin8Low;      // 8일 후 예상최저기온 하한범위
        private Integer taMin8High;     // 8일 후 예상최저기온 상한범위
        private Integer taMax8;         // 8일 후 예상최고기온(℃)
        private Integer taMax8Low;      // 8일 후 예상최고기온 하한범위
        private Integer taMax8High;     // 8일 후 예상최고기온 상한범위
        
        // 9일 후 예상기온
        private Integer taMin9;         // 9일 후 예상최저기온(℃)
        private Integer taMin9Low;      // 9일 후 예상최저기온 하한범위
        private Integer taMin9High;     // 9일 후 예상최저기온 상한범위
        private Integer taMax9;         // 9일 후 예상최고기온(℃)
        private Integer taMax9Low;      // 9일 후 예상최고기온 하한범위
        private Integer taMax9High;     // 9일 후 예상최고기온 상한범위
        
        // 10일 후 예상기온
        private Integer taMin10;        // 10일 후 예상최저기온(℃)
        private Integer taMin10Low;     // 10일 후 예상최저기온 하한범위
        private Integer taMin10High;    // 10일 후 예상최저기온 상한범위
        private Integer taMax10;        // 10일 후 예상최고기온(℃)
        private Integer taMax10Low;     // 10일 후 예상최고기온 하한범위
        private Integer taMax10High;    // 10일 후 예상최고기온 상한범위
    }
}
