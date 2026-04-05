package com.finance.dto.request;

import com.finance.domain.entity.Role;
import com.finance.domain.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.Set;

@Data
public class UpdateUserRequest {
    @Email
    private String email;
    @Size(max = 100)
    private String fullName;
    @Size(min = 6, max = 100)
    private String password;
    private User.UserStatus status;
    private Set<Role.RoleName> roles;
}
