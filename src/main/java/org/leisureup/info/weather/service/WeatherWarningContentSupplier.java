package org.leisureup.info.weather.service;

import java.util.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.leisureup.info.weather.dto.*;
import org.leisureup.info.weather.service.WeatherWarningContentSupplierUtils.*;
import org.springframework.stereotype.*;

/**
 * 기상 특보 정보를 파싱, 정제해 제공해주는 {@code component}
 */
@Slf4j
@Component
public class WeatherWarningContentSupplier {

    /**
     * 특보 내용을 파싱, {@link SingleWeatherWarning} 로 정제해 제공
     *
     * @param singleContent {@code o 풍랑주의보 : 남해동부바깥먼바다, 제주도남쪽바깥먼바다, 제주도남동쪽안쪽먼바다, 제주도남서쪽안쪽먼바다}
     */
    public SingleWeatherWarning parseSingleContent(String singleContent) {

        String[] split = singleContent.split(" : ");

        String warningType = split[0];
        List<String> recommendations = this.getRecommendationsOnType(warningType);
        List<String> regions = this.parseRegions(split[1]);

        return SingleWeatherWarning.of(
                warningType, recommendations, regions
        );
    }

    /**
     * 특보 지역 내용을 파싱해 제공
     * @param regions {@code 남해동부바깥먼바다, 제주도남쪽바깥먼바다, 제주도남동쪽안쪽먼바다, 제주도남서쪽안쪽먼바다}
     * @return {@code 남해동부바깥먼바다}, {@code 제주도남쪽바깥먼바다} {@code ...} 로 이루어진 {@code List}
     */
    private List<String> parseRegions(String regions) {

        List<String> regionList = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int depth = 0;

        for (char c : regions.toCharArray()) {
            if (c == ',' && depth == 0) {
                regionList.add(current.toString().trim());
                current.setLength(0);
            } else {
                if (c == '(') {
                    depth++;
                }
                if (c == ')') {
                    depth--;
                }
                current.append(c);
            }
        }

        if (!current.isEmpty()) {
            regionList.add(current.toString().trim());
        }

        return regionList;
    }

    /**
     * 특보 종류에 따른 주의사항을 제공
     * @param parsedWarningType {@code o 풍랑주의보}, {@code o 호우경보} 등
     */
    private List<String> getRecommendationsOnType(String parsedWarningType) {

        if (parsedWarningType == null) {
            return Collections.emptyList();
        }

        WarningType detected = Arrays.stream(WarningType.values())
                .filter(t -> parsedWarningType.contains(t.getBriefType()))
                .findFirst()
                .orElse(null);

        return detected == null ?
                Collections.emptyList() :
                WeatherWarningContentSupplierUtils.getRecommendations(detected);
    }
}

class WeatherWarningContentSupplierUtils {

    private static final Map<WarningType, List<String>> recommends;

    static {
        recommends = new HashMap<>();
        recommends.put(WarningType.HEAT_WAVE, List.of(
                "한낮 야외 활동은 피해주시고, 휴식을 자주 취하세요",
                "갈증을 느끼지 않아도 수분을 충분히 섭취하세요",
                "어지럼증·근육 경련 등 열사병 증상 발생 시 활동을 즉시 멈추세요",
                "밝은 색 옷차림과 챙이 넓은 모자를 착용하세요"
        ));
        recommends.put(WarningType.GALE, List.of(
                "산악·해안가 활동은 위험하니 자제해주세요",
                "낙하물에 주의하고, 야외 구조물 주변을 피하세요",
                "텐트, 타프, 장비는 단단히 고정해주세요",
                "드론·패러글라이딩 등 고공 레저는 중단하세요"
        ));
        recommends.put(WarningType.STORM, List.of(
                "해상 레저는 즉시 중단해주세요",
                "방파제·해안도로 접근은 매우 위험해요",
                "선박 이용 시 출항 전 반드시 운항 정보를 확인하세요",
                "파도에 휩쓸릴 수 있는 바위 위·갯바위는 피해주세요"
        ));
        recommends.put(WarningType.DOWNPOUR, List.of(
                "천둥·번개 동반 시 수상·야외 활동은 중지하세요",
                "하천·계곡 인근 접근은 삼가주세요",
                "급류·침수 위험 지역은 즉시 대피하세요",
                "고립될 수 있는 산간·계곡 캠핑은 피해주세요"
        ));
        recommends.put(WarningType.DRY, List.of(
                "산불 위험이 높으니 취사·흡연은 자제해주세요",
                "바람 부는 날씨엔 드론·불꽃놀이도 위험해요",
                "불씨는 완전히 꺼졌는지 꼭 확인해주세요",
                "불조심 안내 방송이 있는 경우 즉시 확인해주세요"
        ));
        recommends.put(WarningType.TYPHOON, List.of(
                "모든 야외·수상 활동을 즉시 중지해주세요",
                "차량은 안전한 장소에 주차하세요",
                "최신 기상정보를 지속적으로 확인하세요",
                "숙소나 실내 공간에서 대기하는 것이 가장 안전해요"
        ));
        recommends.put(WarningType.STORM_SURGE, List.of(
                "해안가·해수욕장은 절대 접근하지 마세요",
                "서핑, 카약 등 수상 스포츠는 즉시 중단하세요",
                "고지대나 내륙으로 대피 준비를 해주세요",
                "해안 캠핑은 즉시 철수하고, 도로 접근도 피해주세요"
        ));
        recommends.put(WarningType.HEAVY_SNOW, List.of(
                "스키·눈썰매 외 레저는 노면 미끄러움에 주의하세요",
                "차량 이동 시 체인·겨울 장비를 준비해주세요",
                "계단, 비탈길에서 미끄럼 사고 주의가 필요해요",
                "산행 중 눈사태 위험이 있으니 공식 통제구역을 확인하세요"
        ));
        recommends.put(WarningType.COLD_WAVE, List.of(
                "장시간 외부 활동은 삼가주시고 보온에 유의하세요",
                "저체온증 증상(떨림·무기력 등) 발생 시 즉시 대피하세요",
                "노약자 동반 시 난방과 의류를 충분히 준비해주세요",
                "캠핑 시 난방기 사용은 환기 상태를 반드시 확인하세요"
        ));
        recommends.put(WarningType.YELLOW_DUST, List.of(
                "격한 야외 활동은 피하고 KF94 마스크를 착용하세요",
                "안구·호흡기 질환자, 어린이는 외부 활동을 자제해주세요",
                "실외 장비는 실내 보관이 안전해요",
                "외부 활동 후 손·얼굴을 꼭 씻고 코를 세척해주세요"
        ));
    }

    static List<String> getRecommendations(WarningType type) {
        return recommends.get(type);
    }

    @Getter
    @RequiredArgsConstructor
    enum WarningType {
        HEAT_WAVE("폭염"),
        GALE("강풍"),
        STORM("풍랑"),
        DOWNPOUR("호우"),
        DRY("건조"),
        TYPHOON("태풍"),
        STORM_SURGE("폭풍해일"),
        HEAVY_SNOW("대설"),
        COLD_WAVE("한파"),
        YELLOW_DUST("황사");
        private final String briefType;
    }
}