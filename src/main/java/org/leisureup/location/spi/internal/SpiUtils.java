package org.leisureup.location.spi.internal;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import org.leisureup.global.exception.*;

public class SpiUtils {

    public static <T> NotFound throwNotFoundWithMissingIds(
            List<Long> requiredIds, List<T> searchedEntities,
            Function<T, Long> entityIdMapper
    ) {

        Set<Long> searchedIds = searchedEntities.stream()
                .map(entityIdMapper)
                .collect(Collectors.toSet());

        List<Long> missingIds = requiredIds.stream()
                .filter(id -> !searchedIds.contains(id))
                .toList();

        String msg = String.format(
                "ID [%s] 에 해당하는 장소를 찾을 수 없습니다.",
                missingIds.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(", "))
        );

        return new NotFound(msg);
    }
}
