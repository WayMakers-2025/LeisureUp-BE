package org.leisureup.location.spi;

public record CategoryInfo(
        Long id,
        String name,
        Cat category,
        String recommendingCode
) {

}
