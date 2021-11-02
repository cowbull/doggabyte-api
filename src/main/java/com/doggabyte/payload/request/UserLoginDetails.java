package com.doggabyte.payload.request;
//autoLogin: true
//password: "admin"
//type: "account"
//username: "admin"
public class UserLoginDetails {
    private String autoLogin;
    private String type;
    private String password;
    private String username;

    public String getAutoLogin() {
        return autoLogin;
    }

    public void setAutoLogin(String autoLogin) {
        this.autoLogin = autoLogin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
