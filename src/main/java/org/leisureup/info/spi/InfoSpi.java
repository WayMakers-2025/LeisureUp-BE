package org.leisureup.info.spi;

public interface InfoSpi {

    /**
     * Gps 위도 {@code (x)}, 경도 {@code (y)} 에 해당하는 분류 코드를 제공
     * <p>
     * 내부적으로 {@code KNN} 알고리즘을 통해 좌표에 해당하는 분류 코드를 판별함.
     *
     * @param codeType 분류 코드 타입
     */
    String getCodeOn(double x, double y, CodeType codeType);

}
