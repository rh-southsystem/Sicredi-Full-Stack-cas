package br.com.southsystem.receita.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorDetails {

    private LocalDateTime timestamp;
    private String type;
    private String internalCode;
    private String message;
    private String path;
    private String details;
}
