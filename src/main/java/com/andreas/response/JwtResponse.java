package com.andreas.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    String token;
    String type;
    Long id;
    String username;
    String email;
    List<String> roles;

    public JwtResponse(String token, Long id, String username, String email, List<String> roles,String type) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.type = type;
    }
}
