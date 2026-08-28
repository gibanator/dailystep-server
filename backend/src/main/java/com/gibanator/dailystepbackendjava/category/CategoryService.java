package com.gibanator.dailystepbackendjava.category;

import com.gibanator.dailystepbackendjava.category.exception.CategoryAlreadyExistsException;
import com.gibanator.dailystepbackendjava.category.exception.CategoryNotFoundException;
import com.gibanator.dailystepbackendjava.user.UserEntity;
import com.gibanator.dailystepbackendjava.user.UserRepository;
import com.gibanator.dailystepbackendjava.auth.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryEntity create(Long userId, String name) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (categoryRepository.existsByUserIdAndNameAndDeletedFalse(userId, name)) {
            throw new CategoryAlreadyExistsException(name);
        }

        CategoryEntity cat = new CategoryEntity();

        cat.setName(name);
        cat.setUser(user);

        return categoryRepository.save(cat);
    }

    public List<CategoryEntity> findByUserId(Long userId) {
       return categoryRepository.findByUserIdAndDeletedFalse(userId);
    }

    public CategoryEntity update(UUID id, Long userId, String name, Boolean isActive, Boolean isVisible) {
        CategoryEntity cat = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        if (!cat.getUser().getId().equals(userId) || cat.isDeleted()) {
            throw new CategoryNotFoundException(id);
        }

        if (name != null) {
            cat.setName(name);
        }
        if (isActive != null) {
            cat.setActive(isActive);
        }
        if (isVisible != null) {
            cat.setVisible(isVisible);
        }

        return categoryRepository.save(cat);
    }

    public CategoryEntity switchVisibility(UUID id, Long userId) {
        CategoryEntity cat = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        if (!cat.getUser().getId().equals(userId) || cat.isDeleted()) {
            throw new CategoryNotFoundException(id);
        }

        cat.setVisible(!cat.isVisible());

        return categoryRepository.save(cat);
    }

    public void delete(UUID id, Long userId){
        CategoryEntity cat = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        if (!cat.getUser().getId().equals(userId) || cat.isDeleted()) {
            throw new CategoryNotFoundException(id);
        }

        cat.setDeleted(true);
        cat.setActive(false);
        cat.setVisible(false);
        categoryRepository.save(cat);
    }
}
