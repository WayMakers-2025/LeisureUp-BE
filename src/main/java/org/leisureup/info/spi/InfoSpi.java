package org.leisureup.info.spi;

import org.leisureup.info.weather.service.client.*;

public interface InfoSpi {

    /**
     * Gps 경도 {@code (x)}, 위도 {@code (y)} 에 해당하는 분류 코드를 제공
     * <p>
     * 내부적으로 {@code KNN} 알고리즘을 통해 좌표에 해당하는 분류 코드를 판별함.
     *
     * @param codeType 분류 코드 타입
     */
    String getCodeOn(double x, double y, CodeType codeType);

    /**
     * Gps 경도 {@code (x)}, 위도 {@code (y)} 를 {@code 람베르트 정각원추도법} 으로 변환해 제공
     * <p>
     * 단기 예보 API 에 좌표로 필요.
     *
     * @see ShortTermForecastApi
     */
    LambertProjectionCord convertGpsCord(double x, double y);
}
