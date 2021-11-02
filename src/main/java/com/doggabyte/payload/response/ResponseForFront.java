package com.doggabyte.payload.response;

public class ResponseForFront<O> extends ResponseBasic<O> {
    protected Boolean success = true;

    public Boolean getSuccess() {
        return success;
    }
    public void setSuccess(Boolean success){
        this.success = success;
    }
}
