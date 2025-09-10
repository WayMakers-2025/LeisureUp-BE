package org.leisureup.location.internal.repository.init;

import static org.leisureup.location.internal.repository.init.Utils.*;

import java.util.*;
import lombok.*;
import org.leisureup.location.internal.domain.*;
import org.leisureup.location.internal.domain.AdditionalCategoryInfo.*;

@Getter
@RequiredArgsConstructor
public enum Earth {

    Inline(
            "인라인", "A03020400",
            encodeCodes("ab", "a", "ab", "b", "a"),
            """
                    바퀴가 일렬로 배치된 스케이트를 신고 트랙, 공원, 도심 길 등을 빠르게 주행하는 활동입니다.
                    이동성, 리듬감, 스피드를 동시에 즐길 수 있어 도심 속 가볍게 즐기기 좋습니다.
                    """.trim(),
            "기초 교육만 받으면 누구나 쉽게 체험 가능",
            "인라인 스케이트, 보호대(헬멧, 무릎, 팔꿈치)",
            """
                    보호구 반드시 착용하고 제동법 충분히 연습하기
                    자전거 등 타 이용자와 충돌 위험 주의
                    젖은 노면에서는 미끄러움 주의
                    """.trim(),
            "https://waymakers-from-staging.s3.ap-northeast-2.amazonaws.com/public-assets/backend/inline.jpg",
            List.of(SuitableSeason.ANY)
    ), Bike(
            "자전거 하이킹", "A03020500",
            encodeCodes("ab", "a", "ab", "b", "ad"),
            """
                    산악용 MTB 나 하이브리드 자전거를 이용해 자연 속 트레일이나 하이킹 코스를 주행하는 레저입니다.
                    강변길이나 산악지대 등 코스에 따라 경치와 난이도를 선택할 수 있어 매력적입니다.
                    """.trim(),
            "기본적인 자전거 조작이 가능한 사용자",
            "자전거(MTB 등), 헬멧, 장갑, 라이트",
            """
                    산악 지형은 브레이크·기어 상태 사전 점검
                    야간 라이딩 시 라이트·반사지 필수
                    음료·간식 등 체력 보충 준비 필요
                    """.trim(),
            "https://waymakers-from-staging.s3.ap-northeast-2.amazonaws.com/public-assets/backend/bike.jpg",
            List.of(SuitableSeason.SPRING, SuitableSeason.AUTUMN)
    ), Cart(
            "카트", "A03020600",
            encodeCodes("a", "b", "a", "b", "bd"),
            """
                    전용 서킷에서 엔진이 달린 소형 카트를 운전하며 핸들링과 코너링을 체험하는 액티비티입니다.
                    스피드감과 조종의 손맛이 특징이며, 별도 면허 없이도 즐길 수 있는 모터 레저입니다.
                    """.trim(),
            "안전 교육 후 누구나 체험 가능",
            "고카트, 헬멧, 운전용 장갑",
            """
                    안전 교육 이수 필수, 무리한 가속 자제
                    추돌 방지를 위해 앞차와의 간격 유지
                    트랙 외 주행 금지, 정해진 코스 준수
                    """.trim(),
            "https://waymakers-from-staging.s3.ap-northeast-2.amazonaws.com/public-assets/backend/cart.jpg",
            List.of(SuitableSeason.ANY)
    ), Golf(
            "골프", "A03020700",
            encodeCodes("ab", "a", "a", "bd", "ad"),
            """
                    정해진 홀에 공을 클럽으로 쳐 넣는 스포츠로, 필드 또는 퍼블릭 골프장에서 자연 속에서 여유롭게 즐길 수 있는 레저입니다. 거리 계산, 바람, 지형 등 다양한 요소가 플레이에 영향을 줍니다.
                    """.trim(),
            "입문자부터 경험자까지 모두 참여 가능",
            "골프 클럽, 공, 골프화, 장갑",
            """
                    무더위 시 자외선·탈수 주의
                    타구 시 주변 사람과 거리 확보
                    벙커·그린 등 골프 에티켓 숙지
                    """.trim(),
            "https://waymakers-from-staging.s3.ap-northeast-2.amazonaws.com/public-assets/backend/golf.jpg",
            List.of(SuitableSeason.SPRING, SuitableSeason.AUTUMN)
    ), HorseRiding(
            "승마", "A03021100",
            encodeCodes("a", "a", "a", "b", "acd"),
            """
                    전문 트랙이나 자연 속 산책로에서 말을 타고 걷거나 달리며 교감하는 레저입니다.
                    신체 균형과 집중력이 요구되며, 말과의 교감을 통해 힐링 요소도 큽니다.
                    """.trim(),
            "초보자도 체험 승마 가능, 교육 이수 시 트레킹 가능",
            "헬멧, 승마화, 장갑",
            """
                    탑승 전 말 컨디션과 안전장비 확인
                    돌발 행동·소음에 말이 놀랄 수 있으니 주의
                    자세 및 명령어 숙지, 지시에 따라 행동
                    """.trim(),
            "https://waymakers-from-staging.s3.ap-northeast-2.amazonaws.com/public-assets/backend/horse.jpg",
            List.of(SuitableSeason.SPRING, SuitableSeason.AUTUMN)
    ), SkiSnowboard(
            "스키/스노보드", "A03021200",
            encodeCodes("bc", "ab", "bc", "b", "bcd"),
            """
                    눈 위에서 활주하는 동계 스포츠로, 리조트 내 슬로프에서 즐길 수 있는 활동입니다.
                    스피드감과 밸런스 조절 능력을 함께 요구합니다.
                    """.trim(),
            "초보자도 초급 레슨 후 가능",
            "스키/스노보드, 부츠, 헬멧, 장갑",
            """
                    헬멧·보호장비 착용 필수
                    슬로프 상태·기상 조건 사전 확인
                    넘어짐 시 체온 유지를 위한 보온 준비
                    """.trim(),
            "https://waymakers-from-staging.s3.ap-northeast-2.amazonaws.com/public-assets/backend/snow-board.jpg",
            List.of(SuitableSeason.WINTER)
    ), Skate(
            "스케이트", "A03021300",
            encodeCodes("a", "a", "a", "bd", "bd"),
            """
                    빙판 위에서 얼음을 미끄러지듯 타는 동절기 레저입니다.
                    균형감과 발목 사용 능력을 즐기며 운동량이 높아 체력 향상에도 좋습니다.
                    """.trim(),
            "초보자도 간단한 체험 인솔 후 가능",
            "스케이트화, 헬멧, 무릎·팔꿈치 보호대",
            """
                    보호대 반드시 착용, 넘어짐에 대비
                    붐비는 시간대 피하고 충돌 방지
                    빙판 틈이나 균열 확인 후 입장
                    """.trim(),
            "https://waymakers-from-staging.s3.ap-northeast-2.amazonaws.com/public-assets/backend/skate.jpg",
            List.of(SuitableSeason.WINTER, SuitableSeason.ANY)
    ), Sledding(
            "썰매장", "A03021400",
            encodeCodes("a", "a", "a", "bd", "bd"),
            """
                    인공 슬로프나 자연 언덕에서 썰매를 타는 즐거운 겨울 레저입니다. 경사가 낮고 반복적으로 내려오며,
                    가족 단위로 쉽게 즐길 수 있어 겨울철 인기 체험입니다.
                    """.trim(),
            "남녀노소 누구나 즐길 수 있는 매우 쉬운 레저",
            "썰매, 방한복, 장갑",
            """
                    보호장비 없이도 안전하지만, 어린이는 보호장 착용 권장
                    경사 및 주위 사람과 거리 확보하며 천천히 이용하기
                    슬로프 입·출구에서 넘어짐 주의
                    """.trim(),
            "https://waymakers-from-staging.s3.ap-northeast-2.amazonaws.com/public-assets/backend/sleeding.jpg",
            List.of(SuitableSeason.WINTER)
    ), HuntingGround(
            "수렵장", "A03021500",
            encodeCodes("bc", "b", "bc", "b", "b"),
            """
                    허가된 야외 구역에서 총기를 사용해 사냥 체험을 즐기는 레저입니다. 면허 소지자만 참여 가능하며,
                    자연 생태와 조우하면서 직접적인 사냥 경험을 제공합니다.
                    """.trim(),
            "사냥 면허 및 관련 교육 이수자",
            "총기, 방어용 보호복, 사냥 면허증",
            """
                    면허·허가서류 및 안전 교육 필수 지참
                    총기 방향은 항상 안전 방향으로 관리하기
                    야생동물 예상 이동 경로 주의하며 사격 금지 지역 확인
                    """.trim(),
            "https://waymakers-from-staging.s3.ap-northeast-2.amazonaws.com/public-assets/backend/hunting.jpg",
            List.of(SuitableSeason.ANY)
    ), ShootingRange(
            "사격장", "A03021600",
            encodeCodes("bc", "b", "bc", "bd", "b"),
            """
                    총기를 이용해 목표물을 조준하고 명중시키는 스포츠 레저입니다. 권총, 소총, 공기총 등 다양한 총기를 통해
                    정밀 사격 경험을 제공하며, 실내외 모두 가능할 수 있습니다.
                    """.trim(),
            "사격 안전 교육 이수자",
            "총기, 귀 보호대, 눈 보호 안경",
            """
                    안전 교육 이수 후 착용 금지사항 없는 상태에서만 사용
                    사격 전 과녁 위치 및 주변 사람 여부 최종 확인
                    귀 및 눈 보호 장비는 필수 착용
                    """.trim(),
            "https://waymakers-from-staging.s3.ap-northeast-2.amazonaws.com/public-assets/backend/shooting.jpg",
            List.of(SuitableSeason.ANY)
    ), RockClimbing(
            "암벽등반", "A03021800",
            encodeCodes("bc", "b", "bc", "bd", "a"),
            """
                    자연 바위나 인공벽을 로프와 기구를 사용해 등반하는 스포츠입니다.
                    근력과 정신 집중이 필요하며, 도전의 재미와 완주 후 성취감을 제공합니다.
                    """.trim(),
            "기초 교육 이수자 (초급부터 전문가까지)",
            "하네스, 로프, 카라비너, 클라이밍화, 헬멧",
            """
                    장비사전 상태 점검·파트너와의 호흡 확인
                    낙하 위험 구간에서는 보호자 또는 전문가 동행
                    암벽 상태 변화(습기·조류 등) 체크
                    """.trim(),
            "https://waymakers-from-staging.s3.ap-northeast-2.amazonaws.com/public-assets/backend/climbing.jpg",
            List.of(SuitableSeason.ANY)
    ), SurvivalGaming(
            "서바이벌게임", "A03022000",
            encodeCodes("b", "b", "bc", "b", "b"),
            """
                    페인트볼이나 에어소프트건을 이용해 장애물 코스 안에서 전투 전략을 즐기는 활동입니다.
                    팀워크와 공간 활용 감각이 중요하며, 어린이부터 어른까지 다양하게 참여할 수 있습니다.
                    """.trim(),
            "체력과 기본 안전 수칙 이해자",
            "마스크, 페인트볼/BB탄 장비, 안전 복장",
            """
                    얼굴·눈 보호용 장비 반드시 착용
                    안전 구간 이외 사격 금지
                    가벼운 충돌에도 몸 보호할 수 있는 복장 선택
                    """.trim(),
            "https://waymakers-from-staging.s3.ap-northeast-2.amazonaws.com/public-assets/backend/survival.jpg",
            List.of(SuitableSeason.ANY)
    ), ATV(
            "ATV", "A03022100",
            encodeCodes("bc", "b", "bc", "b", "b"),
            """
                    4륜 소형 엔진 차량을 타고 오프로드 코스에서 주행하는 액티비티입니다.
                    이색적인 지형을 직접 탐험하며 속도감과 스릴을 느낄 수 있습니다.
                    """.trim(),
            "안전 교육 및 초보자 교육 이수자",
            "ATV, 풀페이스 헬멧, 장갑, 보호복",
            """
                    지정된 코스에서만 운전, 과속 금지
                    험지 주행 전 보호장비 착용 필수
                    탄력적 제동과 좌우 핸들 조작 연습 필요
                    """.trim(),
            "https://waymakers-from-staging.s3.ap-northeast-2.amazonaws.com/public-assets/backend/atv.jpg",
            List.of(SuitableSeason.SPRING, SuitableSeason.AUTUMN)
    ), MTB(
            "MTB", "A03022200",
            encodeCodes("bc", "b", "bc", "b", "b"),
            """
                    산악 지형이나 비포장 도로에서 산악 자전거를 타며 자연 속 트레일을 완주하는 레저입니다.
                    다양한 지형 조건에 대응하는 기술과 체력이 요구됩니다.
                    """.trim(),
            "기본 자전거 이용 가능자",
            "MTB, 헬멧, 글러브, 손목 보호대",
            """
                    브레이크·타이어 압력 등 사전 점검
                    급경사는 천천히 통과, 균형 유지
                    넘어졌을 때 손·팔 부상에 대비
                    """.trim(),
            "https://waymakers-from-staging.s3.ap-northeast-2.amazonaws.com/public-assets/backend/mtb.jpg",
            List.of(SuitableSeason.SPRING, SuitableSeason.AUTUMN)
    ), OffRoad(
            "오프로드", "A03022300",
            encodeCodes("bc", "b", "bc", "b", "b"),
            """
                    비포장 도로나 산악 지역을 4륜구동 차량으로 주행하는 모험 레저입니다.
                    진흙, 바위, 물웅덩이 등을 넘어가며 차량 조작 능력과 자연 탐험의 묘미를 함께 즐길 수 있습니다.
                    """.trim(),
            "숙련된 운전자 또는 동승 체험자",
            "오프로드 차량, 안전벨트, 차량 하체 보호 장비",
            """
                    경사·미끄럼 등 지형 특성 파악 후 운전
                    차량 전조등·타이어·제동 상태 사전 점검
                    현지 가이드 동행 시 안전 수칙 철저히 따르기
                    """.trim(),
            "https://waymakers-from-staging.s3.ap-northeast-2.amazonaws.com/public-assets/backend/off-road.jpg",
            List.of(SuitableSeason.SPRING, SuitableSeason.SUMMER, SuitableSeason.AUTUMN)
    ), BungeeJumping(
            "번지점프", "A03022400",
            encodeCodes("bc", "b", "c", "b", "a"),
            """
                    높은 타워나 다리에서 로프에 연결된 채 아래로 뛰어내리는 고강도 익스트림 스포츠입니다.
                    심리적 스릴과 해방감을 동시에 체험할 수 있습니다.
                    """.trim(),
            "신체 건강하고 공포심 조절 가능한 성인",
            "하네스, 점프용 로프, 안전 벨트",
            """
                    체중·신장 제한 여부 확인
                    점프 전 장비 점검 및 안전 브리핑 필수
                    고소공포증, 심장질환자는 체험 자제
                    """.trim(),
            "https://waymakers-from-staging.s3.ap-northeast-2.amazonaws.com/public-assets/backend/jump.jpg",
            List.of(SuitableSeason.SPRING, SuitableSeason.SUMMER, SuitableSeason.AUTUMN)
    ), Tracking(
            "트래킹", "A03022700",
            encodeCodes("a", "a", "a", "b", "acd"),
            """
                    산이나 자연 경로를 따라 장시간 걷는 활동으로, 경치를 즐기며 체력을 기르고 자연과 교감할 수 있는 레저입니다. 코스에 따라 난이도가 다릅니다.
                    """.trim(),
            "기본 체력을 갖춘 누구나 가능",
            "트래킹화, 배낭, 방수복, 간식 및 물",
            """
                    갑작스러운 날씨 변화에 대비해 방한·방수 장비 준비
                    무리한 속도보다는 일정한 보폭 유지
                    낙석·미끄러운 지형 등 위험 구간 주의
                    """.trim(),
            "https://waymakers-from-staging.s3.ap-northeast-2.amazonaws.com/public-assets/backend/tracking.jpg",
            List.of(SuitableSeason.SPRING, SuitableSeason.AUTUMN)
    );

    final String name, code, recommendCode;
    final String briefInfo, target, requiredGear, warning;
    final String thumbnail;
    final List<SuitableSeason> suitableSeasons;

    public CatEarth toEntity() {
        return CatEarth.of(
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
