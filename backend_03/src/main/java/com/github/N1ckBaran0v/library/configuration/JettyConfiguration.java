package com.github.N1ckBaran0v.library.configuration;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.embedded.JettyVirtualThreadsWebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JettyConfiguration {
    @Bean
    public JettyVirtualThreadsWebServerFactoryCustomizer jettyVirtualThreadsWebServerFactoryCustomizer(@NotNull ServerProperties serverProperties) {
        return new JettyVirtualThreadsWebServerFactoryCustomizer(serverProperties);
    }
}
