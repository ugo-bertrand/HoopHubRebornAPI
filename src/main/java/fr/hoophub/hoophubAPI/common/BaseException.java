package fr.hoophub.hoophubAPI.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class BaseException extends Exception{
    private String message;
    private HttpStatus httpStatus;
}
