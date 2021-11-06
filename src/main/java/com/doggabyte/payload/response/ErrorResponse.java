package com.doggabyte.payload.response;

import java.util.Date;

public class ErrorResponse
{
    private int errorCode;
    private String errorMessage;
    private boolean success;
    private Date timestamp;

    public ErrorResponse(){
    }

    public ErrorResponse(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ErrorResponse(int errorCode, String errorMessage, boolean success) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.success = success;
    }

    public ErrorResponse(int errorCode, String errorMessage, boolean success, Date timestamp) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.success = success;
        this.timestamp = timestamp;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
