package org.leisureup.global;

import java.lang.annotation.*;

/**
 * Jwt 를 통한 인증이 선택적인 유형.
 * <p>
 * 토큰이 유효하지 않아도 에러를 일으키지 않음.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JwtAuthIfPossible {

}
