package com.gibanator.dailystepbackendjava.category;

import com.gibanator.dailystepbackendjava.category.dto.*;
import com.gibanator.dailystepbackendjava.auth.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @PostMapping
    public ResponseEntity<CreateCategoryResponse> create(
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody CreateCategoryRequest req
    ) {
        CategoryEntity category = service.create(user.getId(), req.getName());

        CreateCategoryResponse resp = new CreateCategoryResponse();
        resp.setId(category.getId());

        return ResponseEntity
                 .status(HttpStatus.CREATED)
                .body(resp);
    }

    @GetMapping
    public ResponseEntity<List<GetCategoryResponse>> getMyCategories(
            @AuthenticationPrincipal UserPrincipal user
            ) {
        List<GetCategoryResponse> resp = service.findByUserId(user.getId())
                .stream()
                .map(cat -> {
                    GetCategoryResponse dto = new GetCategoryResponse();
                    dto.setName(cat.getName());
                    dto.setId(cat.getId());
                    dto.setVisible(cat.isVisible());
                    dto.setActive(cat.isActive());
                    return dto;
                })
                .toList();
        return ResponseEntity
                .ok(resp);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GetCategoryResponse> editMyCategoryById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody EditCategoryRequest req
    ){
        CategoryEntity cat = service.update(
                id,
                user.getId(),
                req.getName(),
                req.isActive(),
                req.isVisible()
        );

        GetCategoryResponse resp = new GetCategoryResponse();
        resp.setId(cat.getId());
        resp.setName(cat.getName());

        return ResponseEntity.ok(resp);
    }

    @PatchMapping("/{id}/visibility/switch")
    public ResponseEntity<CategoryVisibilityResponse> switchVisibility(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        CategoryEntity cat = service.switchVisibility(id, user.getId());

        return ResponseEntity.ok(
                new CategoryVisibilityResponse(cat.getId(), cat.isVisible())
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeCategory(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal user
    ){
        service.delete(id, user.getId());

        return ResponseEntity
                .noContent()
                .build();
    }
}
