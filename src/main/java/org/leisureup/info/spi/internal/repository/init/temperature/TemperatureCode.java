package org.leisureup.info.spi.internal.repository.init.temperature;

import lombok.*;
import org.leisureup.info.spi.internal.domain.*;

@Getter
@RequiredArgsConstructor
public enum TemperatureCode {

    T_01("11B10101", "서울"),
    T_02("11G00401", "서귀포"),
    T_03("11B20201", "인천"),
    T_04("11F20501", "광주"),
    T_05("11B20601", "수원"),
    T_06("21F20801", "목포"),
    T_07("11B20305", "파주"),
    T_08("11F20401", "여수"),
    T_09("11D10301", "춘천"),
    T_10("11F10201", "전주"),
    T_11("11D10401", "원주"),
    T_12("21F10501", "군산"),
    T_13("11D20501", "강릉"),
    T_14("11H20201", "부산"),
    T_15("11C20401", "대전"),
    T_16("11H20101", "울산"),
    T_17("11C20101", "서산"),
    T_18("11H20301", "창원"),
    T_19("11C20404", "세종"),
    T_20("11H10701", "대구"),
    T_21("11C10301", "청주"),
    T_22("11H10501", "안동"),
    T_23("11G00201", "제주"),
    T_24("11H10201", "포항"),
    ;

    final String code, region;

    public TemperatureForecastCode toEntity() {
        return TemperatureForecastCode.of(code, region);
    }
}
