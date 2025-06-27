package org.leisureup.location.internal.repository.init;

import lombok.*;

@Getter
@RequiredArgsConstructor
public enum Other {

    ComplexLeisureSports(
            "복합레포츠", "A03050100",
            ""
    ),
    ;

    final String name, code, recommendCode;

}
