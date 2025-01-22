package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.form.RegisterForm;
import com.github.N1ckBaran0v.library.server.EndpointContext;
import com.github.N1ckBaran0v.library.server.MiniController;
import org.jetbrains.annotations.NotNull;

public class GetRegisterController implements MiniController {
    @Override
    public void handle(@NotNull EndpointContext ctx) {
        ctx.setResponseBody(new RegisterForm());
    }
}
