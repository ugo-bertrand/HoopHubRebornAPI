package fr.hoophub.hoophubAPI.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ExceptionResponse {
    private String message;
    private String status;
    private Integer code;
}
