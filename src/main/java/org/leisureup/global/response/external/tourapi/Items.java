package org.leisureup.global.response.external.tourapi;

import com.fasterxml.jackson.databind.annotation.*;
import java.util.*;

@JsonDeserialize(using = TourApiItemsDeserializer.class)
public record Items<I>(
        List<I> item
) {

}
