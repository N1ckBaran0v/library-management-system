package com.github.N1ckBaran0v.library.service;

import com.github.N1ckBaran0v.library.data.User;
import com.github.N1ckBaran0v.library.form.LoginForm;
import com.github.N1ckBaran0v.library.form.RegisterForm;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UserServiceImplementation implements UserService {
    private final DatabaseService databaseService;
    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> usersSessions = new ConcurrentHashMap<>();

    public UserServiceImplementation(@NotNull DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @Override
    public void register(@NotNull RegisterForm form, @NotNull String sessionId) {
        synchronized (users) {
            if (users.containsKey(sessionId)) {
                var user = users.get(sessionId);
                if (!User.ADMIN_ROLE.equals(user.getRole())) {
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
            if (!users.containsKey(sessionId)) {
                startSession(sessionId, user);
            }
        }
    }

    @Override
    public void login(@NotNull LoginForm form, @NotNull String sessionId) {
        synchronized (users) {
            if (users.containsKey(sessionId)) {
                throw new SessionNotEndedException();
            }
            try {
                var user = databaseService.findUserByUsername(form.getUsername());
                if (!user.getPassword().equals(form.getPassword())) {
                    throw new InvalidLoginDataException();
                }
                startSession(sessionId, user);
            } catch (UserNotFoundException e) {
                throw new InvalidLoginDataException();
            }
        }
    }

    private void startSession(@NotNull String sessionId, @NotNull User user) {
        users.put(sessionId, user);
        var username = user.getUsername();
        if (usersSessions.containsKey(username)) {
            usersSessions.get(username).add(sessionId);
        } else {
            var set = new HashSet<String>();
            set.add(sessionId);
            usersSessions.put(username, set);
        }
    }

    @Override
    public void logout(@NotNull String sessionId) {
        synchronized (users) {
            if (users.containsKey(sessionId)) {
                users.remove(sessionId);
            } else {
                throw new UnauthorizedException();
            }
        }
    }

    @Override
    public void deleteUser(@NotNull String sessionId, @NotNull String username) {
        synchronized (users) {
            var current = getUser(sessionId);
            if (!User.ADMIN_ROLE.equals(current.getRole())) {
                throw new ForbiddenException();
            }
            databaseService.deleteUserByUsername(username);
            if (usersSessions.containsKey(username)) {
                for (var session : usersSessions.get(username)) {
                    users.remove(session);
                }
                usersSessions.remove(username);
            }
        }
    }

    @Override
    public String getUsername(@NotNull String sessionId) {
        return getUser(sessionId).getUsername();
    }

    @Override
    public User getUser(@NotNull String sessionId) {
        if (!users.containsKey(sessionId)) {
            throw new UserNotFoundException();
        }
        return users.get(sessionId);
    }

    @Override
    public User getUser(@NotNull String sessionId, @NotNull String username) {
        try {
            var current = getUser(sessionId);
            if (!User.ADMIN_ROLE.equals(current.getRole())) {
                throw new ForbiddenException();
            }
            return databaseService.findUserByUsername(username);
        } catch (UserNotFoundException e) {
            throw new ForbiddenException();
        }
    }
}
