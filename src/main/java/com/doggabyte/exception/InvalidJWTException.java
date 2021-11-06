package com.doggabyte.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serial;

/**
 * InvalidJWTException is thrown when jwt is invalid
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidJWTException extends Exception {
    @Serial
    private static final long serialVersionUID = 0;

    private final int errorCode;
    private final String errorMessage;

    public InvalidJWTException(final int errorCode, final String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }

    @Override
    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
    }

    public int getCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }

}
