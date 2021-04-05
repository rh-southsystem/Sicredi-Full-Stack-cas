package br.com.southsystem.receita.exception;

import lombok.Getter;

@Getter
public class BaseException extends Exception {

    private static final long serialVersionUID = 1L;

    private String errorCode;

    public BaseException() {
        super();
    }

    public BaseException(String message, Throwable cause, String errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public BaseException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BaseException(Throwable cause, String errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }
}
