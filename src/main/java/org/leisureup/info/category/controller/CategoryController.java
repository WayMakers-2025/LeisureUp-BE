package org.leisureup.info.category.controller;

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
}
