package org.leisureup.location.internal.repository.init;

import static org.leisureup.location.internal.repository.init.Utils.*;

import java.util.*;
import lombok.*;
import org.leisureup.location.internal.domain.*;
import org.leisureup.location.internal.domain.AdditionalCategoryInfo.*;

@Getter
@RequiredArgsConstructor
public enum Sky {

    Skydiving(
            "스카이다이빙", "A03040100",
            encodeCodes("bc", "b", "c", "c", "a"),
            """
                    비행기에서 뛰어내려 낙하한 뒤 일정 고도에서 낙하산을 펴고 착지하는 고난도 익스트림 레저입니다.
                    압도적인 고도감과 자유 낙하의 짜릿함이 특징입니다.
                    """.trim(),
            "신체 건강한 성인(심장·고혈압 환자 제외)",
            "낙하산, 고글, 점프슈트, 헬멧",
            """
                    사전 이론 교육 및 모의 훈련 이수
                    낙하 위치, 착지 지점 명확히 숙지
                    날씨·기류 등 항공 환경 철저히 확인
                    """.trim(),
            "",
            List.of(SuitableSeason.WINTER)
    ), UltralightAircraft(
            "초경량비행", "A03040200",
            encodeCodes("bc", "b", "c", "c", "a"),
            """
                    1~2인용의 초경량 항공기를 조종하거나 조종사와 함께 탑승해 저고도로 비행하며 자연 경관을 감상하는 레저입니다.
                    고요한 비행과 넓은 시야를 동시에 즐길 수 있습니다.
                    """.trim(),
            """
                    체험형: 누구나
                    조종형: 파일럿 자격 소지자
                    """.trim(),
            "초경량 항공기, 통신기기, 보호안경",
            """
                    기상 상황에 따라 즉시 운항 중단 가능
                    바람 방향·세기·시정거리 철저히 확인
                    탑승 전 전체 기체 점검 필수
                    """.trim(),
            "",
            List.of(SuitableSeason.ANY)
    ), HangGlidingParagliding(
            "행글라이딩/패러글라이딩", "A03040300",
            encodeCodes("bc", "b", "c", "c", "a"),
            """
                    행글라이딩은 견고한 프레임 날개를 조종하며 하늘을 활공하는 레저,
                    패러글라이딩은 천 소재 글라이더를 착용한 채 공중에서 부드럽게 나는 체험입니다.
                    모두 고도에서 이륙하여 활공합니다.
                    """.trim(),
            "전문 강사 동행 시 초보도 가능",
            "글라이더(행글/패러), 하네스, 헬멧",
            """
                    비행 코스와 착륙 지점 숙지
                    강풍, 우천 시 비행 절대 금지
                    장비 착용 상태 꼼꼼히 점검
                    """.trim(),
            "",
            List.of(SuitableSeason.ANY)
    ), HotAirBalloon(
            "열기구", "A03040400",
            encodeCodes("a", "a", "a", "c", "cd"),
            """
                    대형 열기구 바스켓에 탑승해 열의 상승력을 이용하여 하늘을 천천히 비행하는 체험형 레저입니다.
                    고요한 하늘 위에서 풍경을 감상하며 여유롭고 비현실적인 분위기를 즐길 수 있습니다.
                    """.trim(),
            "남녀노소 누구나 가능 (체중 제한 있는 경우도 있음)",
            "열기구, 바스켓, 파일럿 조종 장비",
            """
                    바람 방향·속도·기온 등 기상 조건 철저 확인
                    착륙 시 지면 충격 대비해 자세 잡기
                    기구 이동 시 몸을 바깥으로 내밀지 않기
                    """.trim(),
            "",
            List.of(SuitableSeason.ANY)
    );

    final String name, code, recommendCode;
    final String briefInfo, target, requiredGear, warning;
    final String thumbnail;
    final List<SuitableSeason> suitableSeasons;

    public CatSky toEntity() {
        return CatSky.of(
                name, code, recommendCode,
                toAdditionalInfo()
        );
    }

    public AdditionalCategoryInfo toAdditionalInfo() {
        return AdditionalCategoryInfo.of(
                briefInfo, target, requiredGear, warning,
                thumbnail, suitableSeasons
        );
    }
}
