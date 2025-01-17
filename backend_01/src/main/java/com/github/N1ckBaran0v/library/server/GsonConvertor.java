package com.github.N1ckBaran0v.library.server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GsonConvertor implements JsonConvertor {
    private final Gson gson = new Gson();

    @Override
    public String toJson(@NotNull Object obj) {
        return gson.toJson(obj);
    }

    @Override
    public <T> T fromJson(@NotNull InputStream input, @NotNull Class<T> clazz) throws IOException {
        try (var reader = new InputStreamReader(input)) {
            var result = gson.fromJson(reader, clazz);
            if (result == null) {
                throw new JsonSyntaxException("Could not parse json");
            }
            return result;
        } catch (JsonSyntaxException e) {
            throw new ConvertationException();
        }
    }
}
