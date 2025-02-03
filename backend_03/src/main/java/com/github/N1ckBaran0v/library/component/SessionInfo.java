package com.github.N1ckBaran0v.library.component;

import com.github.N1ckBaran0v.library.data.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class SessionInfo {
    private String username;
    private String role = User.UNAUTHORIZED;

    public String getRole() {
        return role;
    }

    public void setRole(@NotNull String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
