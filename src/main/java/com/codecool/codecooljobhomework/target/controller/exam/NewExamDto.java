package com.codecool.codecooljobhomework.target.controller.exam;

import com.codecool.codecooljobhomework.target.entity.exam.Module;
import com.codecool.codecooljobhomework.target.entity.exam.results.Result;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record NewExamDto(
        String mentorEmail,
        String studentEmail,
        Module module,
        LocalDateTime date,
        boolean cancelled,
        boolean success,
        boolean isLastAttemptInModule,
        String comment,
        List<Result> results
) {}
