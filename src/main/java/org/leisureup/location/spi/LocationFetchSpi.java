package org.leisureup.location.spi;

public interface LocationFetchSpi {

    /**
     * {@code locationId} 에 해당하는 장소가 API 상으로 존재하는지 확인.
     * <p>
     * 만약 존재한다면 DB 에 자동으로 저장됨.
     * <p>
     * 만일 이미 DB 에 존재했어도 {@code true} 반환함.
     *
     * @return 장소가 존재해 저장되었으면 {@code true}
     */
    boolean fetchIfLocationExists(Long locationId);

    /**
     * 어느 장소를 반드시 DB 에 저장해야 하는 event
     */
    void onFetchLocationEvent(FetchLocationEvent event);
}
