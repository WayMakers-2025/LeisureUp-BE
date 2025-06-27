package org.leisureup.location.internal.repository.init;

import static org.leisureup.location.internal.repository.init.Utils.*;

import lombok.*;

@Getter
@RequiredArgsConstructor
public enum Water {

    WindsurfingJetSki(
            "윈드서핑/제트스키", "A03030100",
            encodeCodes("bc", "b", "bc", "a", "b")
    ), KayakCanoe(
            "카약/카누", "A03030200",
            encodeCodes("bc", "b", "bc", "a", "b")
    ), Yacht(
            "요트", "A03030300",
            encodeCodes("bc", "ab", "ac", "a", "bcd")
    ), SnorkelingSkinScubaDiving(
            "스노쿨링/스킨스쿠버다이빙", "A03030400",
            encodeCodes("bc", "", "b", "a", "bc")
    ), FreshwaterFishing(
            "민물낚시", "A03030500",
            encodeCodes("a", "a", "a", "a", "ad")
    ), SeaFishing(
            "바다낚시", "A03030600",
            encodeCodes("a", "a", "a", "a", "ad")
    ), Swimming(
            "수영", "A03030700",
            encodeCodes("a", "a", "a", "ad", "d")
    ), Rafting(
            "래프팅", "A03030800",
            encodeCodes("a", "b", "b", "a", "b")
    );

    final String name, code, recommendCode;

}
