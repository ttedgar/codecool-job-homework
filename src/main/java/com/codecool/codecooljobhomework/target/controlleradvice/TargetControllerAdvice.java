package com.codecool.codecooljobhomework.target.controlleradvice;

import com.codecool.codecooljobhomework.target.controlleradvice.exception.*;
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

//    @ResponseBody
//    @ExceptionHandler(UnableToParseJsonToMapException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String unableToParseJsonToMapException(UnableToParseJsonToMapException unableToParseJsonToMapException) {
//        return unableToParseJsonToMapException.getMessage();
//    }
//
//    @ResponseBody
//    @ExceptionHandler(InvalidDateFormatException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String invalidDateFormatException(InvalidDateFormatException invalidDateFormatException) {
//        return invalidDateFormatException.getMessage();
//    }
//
//    @ResponseBody
//    @ExceptionHandler(MissingFieldException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String missingFieldException(MissingFieldException missingFieldException) {
//        return missingFieldException.getMessage();
//    }
//
//    @ResponseBody
//    @ExceptionHandler(InvalidDimensionException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String invalidDimensionException(InvalidDimensionException invalidDimensionException) {
//        return invalidDimensionException.getMessage();
//    }
//
//    @ResponseBody
//    @ExceptionHandler(InvalidEmailException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String invalidEmailException(InvalidEmailException invalidEmailException) {
//        return invalidEmailException.getMessage();
//    }
//
//    @ResponseBody
//    @ExceptionHandler(InvalidResultException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String invalidResultException(InvalidResultException invalidResultException) {
//        return invalidResultException.getMessage();
//    }
}
