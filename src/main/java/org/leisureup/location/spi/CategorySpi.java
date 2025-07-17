package org.leisureup.location.spi;

import java.util.*;

public interface CategorySpi {

    /**
     * DB 에 저장된 모든 카테고리 정보를 조회
     */
    List<CategoryInfo> getAllCategories();

    /**
     * 제공된 ID 목록에 해당하는 카테고리들의 정보를 조회
     */
    List<CategoryInfo> getCategoryList(List<Long> categoryIds);

    /**
     * 특정 카테고리의 세부 정보를 조회
     */
    DetailedCategoryInfo getCategoryDetail(Long categoryId);
}
