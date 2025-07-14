package org.leisureup.location.spi;

import java.util.*;

public record CategoryInfo(
        Long id,
        String name,
        String thumbnailUrl,
        Cat category,
        String recommendingCode,
        Set<Season> suitableSeasons
) {

}
