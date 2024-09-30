package com.codecool.codecooljobhomework.target.controller;

import com.codecool.codecooljobhomework.target.exception.CodecoolerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class TargetControllerAdvice {

    @ResponseBody
    @ExceptionHandler(CodecoolerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String codecoolerNotFoundException(CodecoolerNotFoundException codecoolerNotFoundException) {
        return codecoolerNotFoundException.getMessage();
    }
}
