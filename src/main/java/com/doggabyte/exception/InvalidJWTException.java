package com.doggabyte.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * InvalidJWTException is thrown when jwt is invalid
 */
public class InvalidJWTException extends Exception {
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

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
