package com.github.N1ckBaran0v.library.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan(basePackages = "com.github.N1ckBaran0v.library")
public class ApplicationConfiguration implements WebMvcConfigurer {
}
