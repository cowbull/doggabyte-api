package com.doggabyte.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * UserNotFoundException is thrown when the user is not found in the database.
 */
public class UserNotFoundException extends Exception {
    private final String errorCode;
    private final String errorMessage;

    public UserNotFoundException(final String errorCode, final String errorMessage) {
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

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
