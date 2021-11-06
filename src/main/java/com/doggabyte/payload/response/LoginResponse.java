package com.doggabyte.payload.response;

public class LoginResponse<O> extends ResponseBasic<O> {
    protected Boolean success = true;

    public Boolean getSuccess() {
        return success;
    }
    public void setSuccess(Boolean success){
        this.success = success;
    }
}
