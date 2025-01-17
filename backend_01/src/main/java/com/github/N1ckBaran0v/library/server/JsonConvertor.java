package com.github.N1ckBaran0v.library.server;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

public interface JsonConvertor {
    String toJson(@NotNull Object obj);
    <T> T fromJson(@NotNull InputStream input, @NotNull Class<T> clazz) throws IOException;
}
