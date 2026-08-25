package com.gibanator.dailystepbackendjava.asr.dto;

public record AsrResponse(
        String text,
        String language,
        Double duration
) {}
