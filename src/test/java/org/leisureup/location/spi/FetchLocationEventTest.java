package org.leisureup.location.spi;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.*;
import org.junit.jupiter.api.*;

class FetchLocationEventTest {

    @Test
    @DisplayName("양수 ID 는 이벤트를 생성할 수 있다.")
    void testOnValidLocationIds() {

        LongStream.rangeClosed(1, 10)
                .boxed()
                .forEach(FetchLocationEvent::new);

    }

    @Test
    @DisplayName("Null 또는 음수 ID 는 이벤트를 생성할 수 없다.")
    void testOnInvalidLocationIds() {

        assertThatThrownBy(() -> new FetchLocationEvent(null))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new FetchLocationEvent(-1L))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new FetchLocationEvent(0L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}