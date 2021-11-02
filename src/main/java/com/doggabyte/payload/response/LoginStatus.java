package com.doggabyte.payload.response;

import java.util.List;

// {"status":"ok","type":"account","currentAuthority":"admin"}
public class LoginStatus {
    private String status;
    private String type;
    private String currentAuthority;
    private String token;

    public LoginStatus(String accessToken, String type, List<String> strRoles, String status) {
        this.status = status;
        this.token = accessToken;
        this.type = type;
        if (strRoles == null) {
            this.currentAuthority = "guest";
        }
        else this.currentAuthority = strRoles.get(0);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCurrentAuthority() {
        return currentAuthority;
    }

    public void setCurrentAuthority(String currentAuthority) {
        this.currentAuthority = currentAuthority;
    }
}
