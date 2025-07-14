package org.leisureup.location.internal.repository.init;

import static org.leisureup.location.internal.repository.init.Utils.*;

import java.util.*;
import lombok.*;
import org.leisureup.location.internal.domain.*;
import org.leisureup.location.internal.domain.AdditionalCategoryInfo.*;

@Getter
@RequiredArgsConstructor
public enum Water {

    WindsurfingJetSki(
            "윈드서핑/제트스키", "A03030100",
            encodeCodes("bc", "b", "bc", "a", "b"),
            """
                    윈드서핑은 바람을 이용해 돛을 조종하며 보드를 타는 수상 스포츠입니다.
                    반면 제트스키는 엔진이 달린 수상 오토바이로 빠른 속도감을 즐기는 레저입니다.
                    """.trim(),
            """
                    윈드서핑: 기초 교육 이수자
                    제트스키: 초보도 가능하나 사전 안전 교육 필수
                    """.trim(),
            """
                    윈드서핑: 보드, 돛, 하네스
                    제트스키: 제트스키 본체, 구명조끼
                    """.trim(),
            """
                    해양 기상 확인 후 출발, 구명조끼 착용 필수
                    출발·회전 시 주변 수상객 거리 확보
                    눈에 띄는 복장 착용으로 사고 예방
                    """.trim(),
            "",
            List.of(SuitableSeason.SUMMER)
    ), KayakCanoe(
            "카약/카누", "A03030200",
            encodeCodes("bc", "b", "bc", "a", "b"),
            """
                    노를 저어가며 강, 호수, 해안가 등을 천천히 탐험하는 수상 레저입니다.
                    카약은 양날 노, 카누는 한쪽 노를 사용하며 조종감의 차이가 있습니다.
                    """.trim(),
            "누구나 가능, 특히 잔잔한 수면에서는 초보자도 안전",
            "카약/카누, 패들, 구명조끼, 방수 가방",
            """
                    급류·기상악화 시 육지로 즉시 대피
                    해가 지기 전 복귀, 조류 및 바람 방향 주의
                    반드시 구명조끼 착용
                    """.trim(),
            "",
            List.of(SuitableSeason.SUMMER)
    ), Yacht(
            "요트", "A03030300",
            encodeCodes("bc", "ab", "ac", "a", "bcd"),
            """
                    돛이나 엔진을 이용해 바다 위를 운항하며 휴식과 여유를 즐기는 프리미엄 수상 레저입니다.
                    관광형 요트부터 직접 조종하는 세일링까지 다양합니다.
                    """.trim(),
            "승선 체험은 누구나 가능, 조종은 면허 보유자",
            "요트, 구명조끼, 선글라스",
            """
                    멀미약 등 준비, 장시간 항해 시 기상 예보 필수 확인
                    출항 전 선내 안전 수칙 숙지
                    날씨 급변 시 선장 지시에 즉시 따르기
                    """.trim(),
            "",
            List.of(SuitableSeason.SUMMER)
    ), SnorkelingSkinScubaDiving(
            "스노쿨링/스킨스쿠버다이빙", "A03030400",
            encodeCodes("bc", "", "b", "a", "bc"),
            """
                    스노쿨링은 얕은 바다에서 마스크와 스노클로 호흡하며 수중 생물을 관찰하는 활동입니다.
                    스킨스쿠버다이빙은 공기탱크 등을 착용하고 바닷속 깊이 잠수해 해양 탐사를 하는 전문 레저입니다.
                    """.trim(),
            """
                    스노쿨링: 누구나 가능
                    스쿠버다이빙: 교육 이수자 및 자격증 보유자
                    """.trim(),
            """
                    스노쿨링: 스노클, 마스크, 핀, 구명조끼
                    스쿠버: 공기탱크, 조절기, 웨트슈트, 핀
                    """.trim(),
            """
                    기압차·이압 대비하여 귀압 조절 숙지
                    스노쿨링 시 구명조끼 필수 착용
                    수중 생물 접촉 금지, 혼자 입수 금지
                    """.trim(),
            "",
            List.of(SuitableSeason.SUMMER, SuitableSeason.AUTUMN)
    ), FreshwaterFishing(
            "민물낚시", "A03030500",
            encodeCodes("a", "a", "a", "a", "ad"),
            """
                    강, 호수, 계곡 등에서 낚싯대를 드리워 물고기를 잡는 활동으로, 조용한 환경 속 자연과 함께하는 휴식형 레저입니다.
                    캠핑과 함께 즐기기에도 적합하며, 난이도도 낮아 초보자에게 인기가 많습니다.
                    """.trim(),
            "남녀노소 누구나 가능",
            "낚싯대, 찌, 미끼, 구명조끼, 쿨러",
            """
                    물가 가장자리는 미끄러우므로 주의
                    수질·기상·수위 등 환경 변화 점검
                    쓰레기 및 미끼 잔여물 정리 철저히 하기
                    """.trim(),
            "",
            List.of(SuitableSeason.SUMMER, SuitableSeason.AUTUMN)
    ), SeaFishing(
            "바다낚시", "A03030600",
            encodeCodes("a", "a", "a", "a", "ad"),
            """
                    배 위나 방파제, 선착장 등 바닷가에서 바다 생물을 낚는 레저입니다.
                    낚는 어종의 크기와 종류가 다양해 손맛이 강하고 레저로서의 성취감이 큽니다.
                    """.trim(),
            "누구나 가능 (배낚시는 멀미약 권장)",
            "바다낚싯대, 릴, 구명조끼, 미끼",
            """
                    조류·파도 정보 사전 확인
                    구명조끼 착용은 필수
                    낚싯바늘 취급 및 날카로운 도구 보관에 주의
                    """.trim(),
            "",
            List.of(SuitableSeason.SUMMER, SuitableSeason.AUTUMN)
    ), Swimming(
            "수영", "A03030700",
            encodeCodes("a", "a", "a", "ad", "d"),
            """
                    해변, 계곡, 수영장 등에서 물속을 자유롭게 헤엄치며 즐기는 레저입니다.
                    체력 소모가 큰 만큼 휴식과 수분 보충도 병행해야 하며, 안전요원이 있는 곳에서 즐기는 것이 좋습니다.
                    """.trim(),
            "기초 수영 가능자",
            "수영복, 수경, 수모, 구명조끼(야외 필수)",
            """
                    물살, 해류 세기 확인 후 입수
                    반드시 동행자와 함께 수영
                    체온 저하 시 즉시 수면 위로 올라오기
                    """.trim(),
            "",
            List.of(SuitableSeason.SUMMER)
    ), Rafting(
            "래프팅", "A03030800",
            encodeCodes("a", "b", "b", "a", "b"),
            """
                    여러 명이 함께 고무보트를 타고 강의 급류를 내려가는 수상 익스트림 스포츠입니다.
                    팀워크와 순발력이 중요하며, 자연의 힘을 온몸으로 체험할 수 있는 스릴 만점 레저입니다.
                    """.trim(),
            "체력 있는 초보자 및 단체 이용자",
            "래프팅 보트, 노, 헬멧, 구명조끼, 방수화",
            """
                    출발 전 안전 브리핑 필수
                    보트에서 떨어졌을 경우 행동요령 숙지
                    날씨 악화 및 수위 상승 시 활동 중단
                    """.trim(),
            "",
            List.of(SuitableSeason.SUMMER, SuitableSeason.AUTUMN)
    );

    final String name, code, recommendCode;
    final String briefInfo, target, requiredGear, warning;
    final String thumbnail;
    final List<SuitableSeason> suitableSeasons;

    public CatWater toEntity() {
        return CatWater.of(
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
