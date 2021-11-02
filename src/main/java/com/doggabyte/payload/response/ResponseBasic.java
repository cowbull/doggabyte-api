package com.doggabyte.payload.response;

public class ResponseBasic<O> extends Response<O> {
    protected O data;
    public O getData(){
        return data;
    }

    public void setData(O data){
        this.data = data;
    }
}
