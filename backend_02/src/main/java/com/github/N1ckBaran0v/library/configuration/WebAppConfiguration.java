package com.github.N1ckBaran0v.library.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@EnableWebMvc
public class WebAppConfiguration implements WebMvcConfigurer {
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(gsonHttpMessageConverter());
    }

    @Bean
    public GsonHttpMessageConverter gsonHttpMessageConverter() {
        var converter = new GsonHttpMessageConverter();
        converter.setSupportedMediaTypes(List.of(MediaType.APPLICATION_JSON));
        converter.setDefaultCharset(StandardCharsets.UTF_8);
        return converter;
    }
}
