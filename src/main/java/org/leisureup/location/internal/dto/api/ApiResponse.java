package org.leisureup.location.internal.dto.api;

import java.util.*;

/**
 * 외부 API 호출과 관련된 계약
 */
public interface ApiResponse<I> {

    boolean isSuccess();

    boolean isEmpty();

    I getSingleItem();

    List<I> getAllItems();
}
