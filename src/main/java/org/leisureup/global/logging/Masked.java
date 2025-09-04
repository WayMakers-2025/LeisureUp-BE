package org.leisureup.global.logging;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE,
        ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Masked {

}
