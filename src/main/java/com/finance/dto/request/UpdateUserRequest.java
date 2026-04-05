package com.finance.dto.request;

import com.finance.domain.entity.Role;
import com.finance.domain.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.util.Set;

public class UpdateUserRequest {
    @Email
    private String email;
    @Size(max = 100)
    private String fullName;
    @Size(min = 6, max = 100)
    private String password;
    private User.UserStatus status;
    private Set<Role.RoleName> roles;

    public UpdateUserRequest() {}
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public User.UserStatus getStatus() { return status; }
    public void setStatus(User.UserStatus status) { this.status = status; }
    public Set<Role.RoleName> getRoles() { return roles; }
    public void setRoles(Set<Role.RoleName> roles) { this.roles = roles; }
}
