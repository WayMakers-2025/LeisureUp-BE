package org.leisureup.location.spi;

public record Add(
        String briefAddress,
        String detailedAddress,
        String zipcode
) {

    public static Add of(String ad1, String ad2, String zip) {

        ad1 = ad1 == null ? "" : ad1;
        ad2 = ad2 == null ? "" : ad2;
        zip = zip == null ? "" : zip;

        return new Add(ad1, ad2, zip);
    }
}
