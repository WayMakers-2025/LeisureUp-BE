package org.leisureup.map.internal.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.*;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TourApiResponse {
    private Response response;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Header header;
        private Body body;

        @Override
        public String toString() {
            return "Response{" +
                    "header=" + header +
                    ", body=" + body +
                    '}';
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Body {
        @JsonDeserialize(using = TourItemsDeserializer.class)
        private Items items;
        private int numOfRows;
        private int pageNo;
        private int totalCount;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Items {
        private List<Item> item;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        private String addr1; // 주소
        private String addr2; // 상세주소
        private String areacode; // 지역코드
        private String cat1; // 대분류
        private String cat2; // 중분류
        private String cat3; // 소분류
        private Long contentid; // 콘텐츠ID
        private String contenttypeid; // 콘텐츠타입ID
        private String createdtime; // 등록일
        private String dist; // 거리
        private String firstimage; // 대표이미지(원본)
        private String firstimage2; // 대표이미지(썸네일)
        private String cpyrhtDivCd; // 저작권 유형
        private String mapx; // GPS X좌표
        private String mapy; // GPS Y좌표
        private String mlevel; // Map Level
        private String modifiedtime; // 수정일
        private String sigungucode; // 시군구코드
        private String tel; // 전화번호
        private String title; // 제목
        private String lDongRegnCd; // 법정동 시도 코드
        private String lDongSignguCd; // 법정동 시군구 코드
        private String lclsSystm1; // 분류체계 대분류
        private String lclsSystm2; // 분류체계 중분류
        private String lclsSystm3; // 분류체계 소분류
        private String zipcode; // 우편번호
    }
} 