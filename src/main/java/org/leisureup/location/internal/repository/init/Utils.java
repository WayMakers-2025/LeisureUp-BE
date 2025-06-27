package org.leisureup.location.internal.repository.init;

import java.util.*;

class Utils {

    static String encodeCodes(String... codes) {

        if (codes == null || codes.length == 0) {
            return "";
        }

        return String.join(
                "-",
                Arrays.stream(codes).map(c -> c == null ? "" : c)
                        .toArray(String[]::new)
        );
    }
}
