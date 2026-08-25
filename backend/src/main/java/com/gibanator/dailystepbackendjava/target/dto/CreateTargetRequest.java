package com.gibanator.dailystepbackendjava.target.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateTargetRequest(
        @NotBlank(message = "Target name is required.")
        @Size(max = 255, message = "Target name must not exceed 255 characters.")
        String name,

        @Positive(message = "Target days must be greater than zero.")
        int days,

        LocalDate deadline
) {
}
