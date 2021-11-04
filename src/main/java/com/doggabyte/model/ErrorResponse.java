package com.doggabyte.model;

public class ErrorResponse
{
    private int errorCode;
    private String errorMessage;
    private boolean success;

    public ErrorResponse(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ErrorResponse(int errorCode, String errorMessage, boolean success) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.success = success;
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

    public ErrorResponse(){
    }



    public int getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
