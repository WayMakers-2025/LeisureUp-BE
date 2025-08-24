package org.leisureup.info.recommend.dto.response;

import java.io.*;

public record CategoryRecommendation(
        Long categoryId,
        String name,
        String thumbnailUrl
) implements Serializable {

}
