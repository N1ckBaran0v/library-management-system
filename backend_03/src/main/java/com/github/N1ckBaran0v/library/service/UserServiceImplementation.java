package com.github.N1ckBaran0v.library.service;

import com.github.N1ckBaran0v.library.component.SessionInfo;
import com.github.N1ckBaran0v.library.data.User;
import com.github.N1ckBaran0v.library.form.LoginForm;
import com.github.N1ckBaran0v.library.form.RegisterForm;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplementation implements UserService {
    private final DatabaseService databaseService;

    @Autowired
    public UserServiceImplementation(@NotNull DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @Override
    public void register(@NotNull SessionInfo sessionInfo, @NotNull RegisterForm form) {
        if (!User.UNAUTHORIZED.equals(sessionInfo.getRole())) {
            if (!User.ADMIN_ROLE.equals(sessionInfo.getRole())) {
                throw new SessionNotEndedException();
            }
        } else if (!User.USER_ROLE.equals(form.getRole())) {
            throw new ForbiddenException();
        }
        var user = new User();
        user.setUsername(form.getUsername());
        user.setPassword(form.getPassword());
        user.setName(form.getName());
        user.setPhone(form.getPhone());
        user.setRole(form.getRole());
        databaseService.createUser(user);
        if (!(User.ADMIN_ROLE.equals(sessionInfo.getRole()))) {
            startSession(sessionInfo, user);
        }
    }

    @Override
    public void login(@NotNull SessionInfo sessionInfo, @NotNull LoginForm form) {
        if (!User.UNAUTHORIZED.equals(sessionInfo.getRole())) {
            throw new SessionNotEndedException();
        }
        try {
            var user = databaseService.findUserByUsername(form.getUsername());
            if (!user.getPassword().equals(form.getPassword())) {
                throw new InvalidLoginDataException();
            }
            startSession(sessionInfo, user);
        } catch (UserNotFoundException e) {
            throw new InvalidLoginDataException();
        }
    }

    private void startSession(@NotNull SessionInfo sessionInfo, @NotNull User user) {
        sessionInfo.setRole(user.getRole());
        sessionInfo.setUsername(user.getUsername());
    }

    @Override
    public void logout(@NotNull SessionInfo sessionInfo) {
        if (sessionInfo.getUsername() != null) {
            sessionInfo.setUsername(null);
            sessionInfo.setRole(User.UNAUTHORIZED);
        } else {
            throw new UnauthorizedException();
        }
    }

    @Override
    public void deleteUser(@NotNull SessionInfo sessionInfo, @NotNull String username) {
        if (User.UNAUTHORIZED.equals(sessionInfo.getRole())) {
            throw new UnauthorizedException();
        }
        if (!User.ADMIN_ROLE.equals(sessionInfo.getRole())) {
            throw new ForbiddenException();
        }
        databaseService.deleteUserByUsername(username);
    }

    @Override
    public User getUser(@NotNull SessionInfo sessionInfo) {
        if (User.UNAUTHORIZED.equals(sessionInfo.getRole())) {
            throw new UnauthorizedException();
        }
        return databaseService.findUserByUsername(sessionInfo.getUsername());
    }

    @Override
    public User getUser(@NotNull SessionInfo sessionInfo, @NotNull String username) {
        if (User.UNAUTHORIZED.equals(sessionInfo.getRole())) {
            throw new UnauthorizedException();
        }
        if (!(User.ADMIN_ROLE.equals(sessionInfo.getRole()) || username.equals(sessionInfo.getUsername()))) {
            throw new ForbiddenException();
        }
        return databaseService.findUserByUsername(username);
    }
}
