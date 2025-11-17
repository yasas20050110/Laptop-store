package com.soul.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long expiresIn;
    private String username;
    private String email;

    public JwtResponse(String token, Long expiresIn, String username, String email) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.username = username;
        this.email = email;
    }
}
