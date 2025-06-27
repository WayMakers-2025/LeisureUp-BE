package org.leisureup.global;

import java.lang.annotation.*;

/**
 * Jwt 를 통한 인증이 필수적인 유형.
 * <p>
 * 토큰이 유효하지 않으면 반드시 에러를 일으킴.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JwtAuthRequired {

}
