package com.github.N1ckBaran0v.library.configuration;

import com.github.N1ckBaran0v.library.aspect.SecurityAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class SecurityConfiguration {
    @Bean
    public SecurityAspect securityAspect() {
        return new SecurityAspect();
    }
}
