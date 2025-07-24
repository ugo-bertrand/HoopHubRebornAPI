package fr.hoophub.hoophubAPI.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandling {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ExceptionResponse> handleBaseException(BaseException e){
        ExceptionResponse response = new ExceptionResponse(e.getMessage(),e.getHttpStatus().toString(),e.getHttpStatus().value());
        return new ResponseEntity<>(response, e.getHttpStatus());
    }
}
