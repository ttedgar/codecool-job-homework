package com.codecool.codecooljobhomework.target.controller.exam;

import com.codecool.codecooljobhomework.target.entity.exam.Module;
import com.codecool.codecooljobhomework.target.entity.exam.results.Result;

import java.time.LocalDate;
import java.util.List;

public record NewExamDto(
        String mentorEmail,
        String studentEmail,
        Module module,
        LocalDate date,
        boolean cancelled,
        boolean success,
        String comment,
        List<Result> results
) {}
