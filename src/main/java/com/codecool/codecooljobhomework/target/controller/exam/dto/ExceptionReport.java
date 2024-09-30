package com.codecool.codecooljobhomework.target.controller.exam.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class ExceptionReport {
    private String message;
    private Class exceptionClass;

    public ExceptionReport(String message, Class exceptionClass) {
        this.message = message;
        this.exceptionClass = exceptionClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExceptionReport that)) return false;
        return Objects.equals(message, that.message) && Objects.equals(exceptionClass, that.exceptionClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, exceptionClass);
    }
}
