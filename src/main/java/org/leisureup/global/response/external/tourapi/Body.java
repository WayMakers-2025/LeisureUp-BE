package org.leisureup.global.response.external.tourapi;

import java.util.*;
import lombok.extern.slf4j.*;

@Slf4j
public record Body<I>(
        int numOfRows,
        int pageNo,
        int totalCount,
        Items<I> items
) {

    public boolean isValid() {
        if (numOfRows == 0) {
            return items == null || items.item() == null || items.item().isEmpty();
        }

        List<I> itemList;

        return items != null &&
               (itemList = items.item()) != null &&
               itemList.size() == numOfRows;
    }

    public I getSingleItem() {
        if (numOfRows != 1) {
            throw new IllegalStateException("Row isn't a single row");
        }

        if (!isValid()) {
            var e = new IllegalStateException(
                    "Expected row to be single on response, but somehow item isn't"
            );
            log.error("Failed to serve single item", e);
            throw e;
        }

        return items.item().getFirst();
    }

    public List<I> getItems() {
        if (numOfRows == 0) {
            return Collections.emptyList();
        }

        if (!isValid()) {
            var e = new IllegalStateException(
                    "Expected row to be exists, but somehow item isn't"
            );
            log.error("Failed to serve items", e);
            throw e;
        }

        return List.copyOf(items.item());
    }
}
