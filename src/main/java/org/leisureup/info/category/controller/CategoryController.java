package org.leisureup.info.category.controller;

import jakarta.validation.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.leisureup.global.response.*;
import org.leisureup.info.category.dto.response.*;
import org.leisureup.info.category.service.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ApiResponse<GetAllCategoriesResponse> getAllCategories() {
        var resp = categoryService.getAllCategories();
        return ApiResponse.success(200, resp);
    }

    @GetMapping("/{categoryId}")
    public ApiResponse<GetCategoryResponse> getCategory(
            @Valid @Positive @NotNull
            @PathVariable Long categoryId
    ) {
        var resp = categoryService.getCategory(categoryId);
        return ApiResponse.success(200, resp);
    }
}
