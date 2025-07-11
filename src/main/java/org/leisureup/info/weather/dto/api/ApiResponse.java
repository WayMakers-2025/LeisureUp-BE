package org.leisureup.info.weather.dto.api;

import java.util.*;

public interface ApiResponse<I> {

    boolean isSuccess();

    boolean isEmpty();

    I getSingleItem();

    List<I> getAllItems();
}
