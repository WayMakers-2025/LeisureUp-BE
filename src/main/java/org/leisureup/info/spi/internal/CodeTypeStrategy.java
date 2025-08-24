package org.leisureup.info.spi.internal;

import java.util.*;
import java.util.Map.*;
import org.leisureup.info.spi.*;
import org.leisureup.info.spi.internal.domain.*;
import org.springframework.data.domain.*;

public interface CodeTypeStrategy {

    int NEIGHBOR = 5;

    String getCodeOn(double x, double y);

    CodeType getType();

    default PageRequest getPageRequest() {
        return PageRequest.of(0, NEIGHBOR);
    }

    default Comparator<Entry<String, List<Position>>> comparingKnn(
            double x, double y
    ) {

        Comparator<Entry<String, List<Position>>> classSize = Comparator.comparing(
                es -> es.getValue().size()
        );

        Comparator<Entry<String, List<Position>>> closest = Comparator.comparing(
                es -> es.getValue().stream()
                        .mapToDouble(p -> p.getDistFrom(x, y))
                        .min()
                        .orElse(Double.MAX_VALUE)
        );

        return classSize.thenComparing(closest);
    }
}
