package com.gibanator.dailystepbackendjava.category.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetCategoryResponse {
    private Long id;
    private String name;
    private boolean isVisible;
    private boolean isActive;
}
