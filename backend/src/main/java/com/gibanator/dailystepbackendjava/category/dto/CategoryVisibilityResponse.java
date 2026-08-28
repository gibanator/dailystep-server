package com.gibanator.dailystepbackendjava.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CategoryVisibilityResponse {
    private UUID id;
    private boolean visible;
}
