package com.gibanator.dailystepbackendjava.category.exception;

import java.util.Set;


public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long id) {
        super("Category with id " + id + " not found.");
    }

    public CategoryNotFoundException(Set<Long> ids) {
        super("Categories not found: " + ids);
    }
}
