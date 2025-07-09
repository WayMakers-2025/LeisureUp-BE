package org.leisureup.map.internal.dto.request;

import lombok.*;

@Getter
@RequiredArgsConstructor
public enum LeisureFilter {

    // ### EARTH category ###
    Inline(
            "인라인", "A03020400"
    ), Bike(
            "자전거 하이킹", "A03020500"
    ), Cart(
            "카트", "A03020600"
    ), Golf(
            "골프", "A03020700"
    ), HorseRiding(
            "승마", "A03021100"
    ), SkiSnowboard(
            "스키/스노보드", "A03021200"
    ), Skate(
            "스케이트", "A03021300"
    ), Sledding(
            "썰매장", "A03021400"
    ), HuntingGround(
            "수렵장", "A03021500"
    ), AutoCamping(
            "오토캠핑", "A03021700"
    ), ShootingRange(
            "사격장", "A03021600"
    ), RockClimbing(
            "암벽등반", "A03021800"
    ), SurvivalGaming(
            "서바이벌게임", "A03022000"
    ), ATV(
            "ATV", "A03022100"
    ), MTB(
            "MTB", "A03022200"
    ), OffRoad(
            "오프로드", "A03022300"
    ), BungeeJumping(
            "번지점프", "A03022400"
    ), Tracking(
            "트래킹", "A03022700"
    ), // ### WATER category ###
    WindsurfingJetSki(
            "윈드서핑/제트스키", "A03030100"
    ), KayakCanoe(
            "카약/카누", "A03030200"
    ), Yacht(
            "요트", "A03030300"
    ), SnorkelingSkinScubaDiving(
            "스노쿨링/스킨스쿠버다이빙", "A03030400"
    ), FreshwaterFishing(
            "민물낚시", "A03030500"
    ), SeaFishing(
            "바다낚시", "A03030600"
    ), Swimming(
            "수영", "A03030700"
    ), Rafting(
            "래프팅", "A03030800"
    ), // ### SKY category ###
    Skydiving(
            "스카이다이빙", "A03040100"
    ), UltralightAircraft(
            "초경량비행", "A03040200"
    ), HangGlidingParagliding(
            "행글라이딩/패러글라이딩", "A03040300"
    ), HotAirBalloon(
            "열기구", "A03040400"
    );

    private final String catName, fullCode;

    public Code toCode() {
        return new Code(fullCode);
    }

    @Getter
    public static class Code {

        private final String cat1, cat2, cat3;

        public Code(String code) {
            if (code == null || code.length() != 9) {
                throw new IllegalArgumentException("Cannot parse code");
            }

            cat1 = code.substring(0, 3);
            cat2 = code.substring(0, 5);
            cat3 = code;
        }
    }
}
