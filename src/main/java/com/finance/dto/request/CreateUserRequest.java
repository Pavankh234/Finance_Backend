package com.finance.dto.request;

import com.finance.domain.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.Set;

@Data
public class CreateUserRequest {
    @NotBlank @Size(min = 3, max = 50)
    private String username;

    @NotBlank @Email
    private String email;

    @NotBlank @Size(min = 6, max = 100)
    private String password;

    @Size(max = 100)
    private String fullName;

    @NotEmpty
    private Set<Role.RoleName> roles;
}
