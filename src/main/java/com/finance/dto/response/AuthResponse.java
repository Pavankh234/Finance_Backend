package com.finance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String tokenType = "Bearer";
    private Long expiresIn;
    private UserInfo user;

    public AuthResponse(String token, Long expiresIn, UserInfo user) {
        this.token = token; this.expiresIn = expiresIn; this.user = user;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private Long id;
        private String username;
        private String email;
        private String fullName;
        private Set<String> roles;
        private Set<String> permissions;
    }
}
