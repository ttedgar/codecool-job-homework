package com.codecool.codecooljobhomework.target.controlleradvice;

import com.codecool.codecooljobhomework.target.controlleradvice.exception.CodecoolerNotFoundException;
import com.codecool.codecooljobhomework.target.controlleradvice.exception.CodecoolerPositionMismatchException;
import com.codecool.codecooljobhomework.target.controlleradvice.exception.UnableToParseJsonToValidExamObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class targetControllerAdvice {

    @ResponseBody
    @ExceptionHandler(CodecoolerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String codecoolerNotFoundException(CodecoolerNotFoundException codecoolerNotFoundException) {
        return codecoolerNotFoundException.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(CodecoolerPositionMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String codecoolerPositionMismatchException(CodecoolerPositionMismatchException codecoolerPositionMismatchException) {
        return codecoolerPositionMismatchException.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(UnableToParseJsonToValidExamObject.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String codecoolerNotFoundException(UnableToParseJsonToValidExamObject unableToParseJsonToValidExamObject) {
        return unableToParseJsonToValidExamObject.getMessage();
    }
}
