package org.leisureup.info.recommend.internal.service;

/**
 * 두 문자열간 유사성 점수를 제공하는 계약
 */
public interface Scoring {

    double score(String one, String second);
}
