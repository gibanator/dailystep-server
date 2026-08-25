package com.gibanator.dailystepbackendjava.category.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditCategoryRequest {

    private String name;
    private boolean isActive;
    // sort order TODO
    private boolean isVisible;

}
