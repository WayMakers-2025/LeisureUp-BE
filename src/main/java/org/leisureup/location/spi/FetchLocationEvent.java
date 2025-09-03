package org.leisureup.location.spi;

public record FetchLocationEvent(
        Long locationId
) {

    public FetchLocationEvent {
        if (locationId == null) {
            throw new IllegalArgumentException("locationId cannot be null");
        }

        if (locationId <= 0L) {
            throw new IllegalArgumentException("locationId must be positive");
        }
    }
}
