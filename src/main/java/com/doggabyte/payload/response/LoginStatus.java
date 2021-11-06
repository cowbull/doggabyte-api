package com.doggabyte.payload.response;

import java.util.List;

// {"status":"ok","type":"account","currentAuthority":"admin"}
public class LoginStatus {
    private String status;
    private String type;
    private String currentAuthority;
    private String token;
    private Long id;
    private String username;
    private String email;

    private String refreshToken;

    public LoginStatus(String accessToken, String type, List<String> strRoles, String status) {
        this.status = status;
        this.token = accessToken;
        this.type = type;
        if (strRoles == null) {
            this.currentAuthority = "guest";
        }
        else this.currentAuthority = strRoles.get(0);
    }

    public LoginStatus(String accessToken,String type, List<String> strRoles, String status,
                       String refreshToken, Long id, String username, String email) {
        this.token = accessToken;
        this.type = type;
        this.status = status;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.email = email;
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
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
