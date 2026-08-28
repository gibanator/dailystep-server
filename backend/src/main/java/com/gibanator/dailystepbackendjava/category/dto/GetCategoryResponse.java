package com.gibanator.dailystepbackendjava.category.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class GetCategoryResponse {
    private UUID id;
    private String name;
    private boolean isVisible;
    private boolean isActive;
}
