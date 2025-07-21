package org.leisureup.info.spi.internal.repository.init.land;

import lombok.*;
import org.leisureup.info.spi.internal.domain.*;

@Getter
@RequiredArgsConstructor
public enum LandCode {

    L_01("11B00000", "서울, 인천, 경기도"),
    L_02("11D10000", "강원도영서"),
    L_03("11D20000", "강원도영동"),
    L_04("11C20000", "대전, 세종, 충청남도"),
    L_05("11C10000", "충청북도"),
    L_06("11F20000", "광주, 전라남도"),
    L_07("11F10000", "전북자치도"),
    L_08("11H10000", "대구, 경상북도"),
    L_09("11H20000", "부산, 울산, 경상남도"),
    L_10("11G00000", "제주도");

    final String code, region;

    public LandForecastCode toEntity() {
        return LandForecastCode.of(code, region);
    }
}
