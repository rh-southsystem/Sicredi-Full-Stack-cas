package br.com.southsystem.receita.exception;


import br.com.southsystem.receita.enums.EventStatusEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import org.springframework.web.client.HttpServerErrorException.BadGateway;
import org.springframework.web.client.HttpServerErrorException.GatewayTimeout;
import org.springframework.web.client.HttpServerErrorException.NotImplemented;
import org.springframework.web.client.HttpServerErrorException.ServiceUnavailable;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(
                buildError(ex, request, ex.getErrorCode()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotImplemented.class)
    public ResponseEntity<?> notImplementedException(NotImplemented ex, WebRequest request) {
        return new ResponseEntity<>(
                buildError(ex, request, EventStatusEnum.GENERIC_ERROR.getCode()),
                HttpStatus.NOT_IMPLEMENTED);
    }

    @ExceptionHandler(BadGateway.class)
    public ResponseEntity<?> badGatawayException(BadGateway ex, WebRequest request) {
        return new ResponseEntity<>(
                buildError(ex, request, EventStatusEnum.GENERIC_ERROR.getCode()),
                HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(BadRequest.class)
    public ResponseEntity<?> badRequestException(BadRequest ex, WebRequest request) {
        return new ResponseEntity<>(
                buildError(ex, request, EventStatusEnum.GENERIC_ERROR.getCode()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GatewayTimeout.class)
    public ResponseEntity<?> badRequestException(GatewayTimeout ex, WebRequest request) {
        return new ResponseEntity<>(
                buildError(ex, request, EventStatusEnum.GENERIC_ERROR.getCode()),
                HttpStatus.GATEWAY_TIMEOUT);
    }

    @ExceptionHandler(ServiceUnavailable.class)
    public ResponseEntity<?> badRequestException(ServiceUnavailable ex, WebRequest request) {
        return new ResponseEntity<>(
                buildError(ex, request, EventStatusEnum.GENERIC_ERROR.getCode()),
                HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(buildError(ex, request, EventStatusEnum.GENERIC_ERROR.getCode()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * @param ex
     * @param request
     * @param code
     * @return
     */
    private ErrorDetails buildError(final Exception ex, final WebRequest request, String code) {
        return ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .type(ex.getClass().getSimpleName())
                .internalCode(code)
                .message(ex.getMessage())
                .path(request.getDescription(Boolean.FALSE))
                .build();
    }

}
