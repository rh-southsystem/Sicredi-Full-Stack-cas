package br.com.southsystem.receita.exception;

import br.com.southsystem.receita.enums.EventStatusEnum;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends BaseException {

    private static final EventStatusEnum status = EventStatusEnum.RESOURCE_NOT_FOUND;

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException() {
        super(status.getMesssage(), status.getCode());
    }

    public ResourceNotFoundException(String message, Throwable cause, String errorCode) {
        super(message, cause, errorCode);
    }

    public ResourceNotFoundException(String message, String errorCode) {
        super(message, errorCode);
    }

    public ResourceNotFoundException(Throwable cause, String errorCode) {
        super(cause, errorCode);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }
}