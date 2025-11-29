package id.taufiq.latihan.spring_ai.controller;

import id.taufiq.latihan.spring_ai.exception.UnsupportedFileFormatException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(UnsupportedFileFormatException.class)
    public ErrorResponse handleUnsupportedFile(UnsupportedFileFormatException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorResponse {
        private String message;
    }
}
