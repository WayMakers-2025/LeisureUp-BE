package org.leisureup.location.spi;

public record Desc(
        String overview,
        String homepage,
        String telephone,
        String largeThumbnail,
        String smallThumbnail
) {

    public static Desc of(String ov, String hm, String tel, String lt, String st) {

        ov = ov == null ? "" : ov;
        hm = hm == null ? "" : hm;
        tel = tel == null ? "" : tel;
        lt = lt == null ? "" : lt;
        st = st == null ? "" : st;

        return new Desc(ov, hm, tel, lt, st);
    }
}
