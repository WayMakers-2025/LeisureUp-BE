package org.leisureup.location.spi;

public record LocationResponse(
        Long locationId,
        String title,
        Gps gps,
        Add address,
        Desc description,
        Cat category
) {

}
