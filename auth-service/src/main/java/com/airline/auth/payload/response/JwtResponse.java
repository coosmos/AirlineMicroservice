package com.airline.auth.payload.response;

import java.util.List;

public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    private boolean pwdExpired;
    public JwtResponse() {
    }
    public JwtResponse(String token, Long id, String username, String email, List<String> roles, boolean pwdExpired) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.pwdExpired = pwdExpired;
    }
    public String getToken() {
        return token;
    }
    public String getType() {
        return type;
    }
    public Long getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public List<String> getRoles() {
        return roles;
    }
    public boolean isPwdExpired() {
        return pwdExpired;
    }
}
