package org.leisureup.global.response.external.base;

import com.fasterxml.jackson.databind.annotation.*;
import java.util.*;

@JsonDeserialize(using = DefaultItemsDeserializer.class)
public record Items<I>(
        List<I> item
) {

}
