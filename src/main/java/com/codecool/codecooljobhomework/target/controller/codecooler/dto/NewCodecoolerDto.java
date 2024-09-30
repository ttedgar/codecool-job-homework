package com.codecool.codecooljobhomework.target.controller.codecooler.dto;

import com.codecool.codecooljobhomework.target.entity.codecooler.Position;

import java.time.LocalDate;

public record NewCodecoolerDto(String email, String username, LocalDate birthday, Position position) {
}
