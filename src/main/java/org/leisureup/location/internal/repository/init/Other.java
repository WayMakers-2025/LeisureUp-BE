package org.leisureup.location.internal.repository.init;

import lombok.*;
import org.leisureup.location.internal.domain.*;

@Getter
@RequiredArgsConstructor
public enum Other {

    ComplexLeisureSports(
            "복합레포츠", "A03050100",
            ""
    ),
    ;

    final String name, code, recommendCode;

    public CatOther toEntity() {
        return CatOther.of(name, code, recommendCode);
    }
}
