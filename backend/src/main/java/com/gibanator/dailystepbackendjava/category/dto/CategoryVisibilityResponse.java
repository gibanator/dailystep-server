package com.gibanator.dailystepbackendjava.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryVisibilityResponse {
    private Long id;
    private boolean visible;
}
