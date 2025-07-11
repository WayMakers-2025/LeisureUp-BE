package org.leisureup.global.response.external;

import java.util.*;

/**
 * 외부 API 응답과 관련된 계약
 *
 * @param <I> 응답으로부터 뽑고싶은 핵심 정보
 */
public interface ExternalApiResponse<I> {

    /**
     * 성공적으로 응답을 가져왔는지 여부
     */
    boolean isSuccess();

    /**
     * 가장 첫번째 핵심 정보를 제공
     */
    I getSingleItem();

    /**
     * 모든 핵심 정보를 제공
     */
    List<I> getItems();

}
