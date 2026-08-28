package com.gibanator.dailystepbackendjava.category.exception;

import java.util.Set;
import java.util.UUID;


public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(UUID id) {
        super("Category with id " + id + " not found.");
    }

    public CategoryNotFoundException(Set<UUID> ids) {
        super("Categories not found: " + ids);
    }
}
