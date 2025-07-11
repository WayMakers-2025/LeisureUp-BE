package org.leisureup.info.weather.dto.api.internal;

import java.util.*;

public record Body<I>(
        String dataType,
        int pageNo,
        int numOfRows,
        int totalCount,
        Items<I> items
) {

    public boolean isEmpty() {
        return numOfRows == 0 || items == null || items.item().isEmpty();
    }

    public I getSingleItem() {
        if (this.isEmpty()) {
            throw new IllegalStateException("No item found");
        }

        List<I> itemList = items.item();

        if (itemList.size() != 1) {
            throw new IllegalStateException("Multiple items found");
        }

        return itemList.getFirst();
    }

    public List<I> getAllItems() {
        if (this.isEmpty()) {
            throw new IllegalStateException("No items found");
        }

        return items.item();
    }
}
