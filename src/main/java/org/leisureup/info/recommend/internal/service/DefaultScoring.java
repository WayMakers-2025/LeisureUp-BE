package org.leisureup.info.recommend.internal.service;

import lombok.extern.slf4j.*;

@Slf4j
public class DefaultScoring implements Scoring {

    private static final String DELIMITER = "-";

    private static String[] parseCode(String code) {
        try {
            return code.split(DELIMITER);
        } catch (Exception e) {
            log.warn("Failed to parse code [{}]. Supplying empty array", code, e);
            return new String[0];
        }
    }

    @Override
    public double score(String one, String second) {
        String[] parsed1 = parseCode(one);
        String[] parsed2 = parseCode(second);

        int len = Math.min(parsed1.length, parsed2.length);
        int score = 0;

        for (int i = 0; i < len; i++) {
            String st1 = parsed1[i];
            String st2 = parsed2[i];

            if (st1.equals(st2)) {
                score += 2;
            } else if (st1.contains(st2) || st2.contains(st1)) {
                score++;
            }
        }

        return score;
    }
}
