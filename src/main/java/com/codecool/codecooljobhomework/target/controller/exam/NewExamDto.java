package com.codecool.codecooljobhomework.target.controller.exam;

import com.codecool.codecooljobhomework.target.entity.exam.Module;
import com.codecool.codecooljobhomework.target.entity.exam.results.Dimension;
import com.codecool.codecooljobhomework.target.entity.exam.results.Result;

import java.time.LocalDateTime;
import java.util.HashMap;
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
        Map<Dimension, Result> results
) {}
