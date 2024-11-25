package com.monolito.monolitica.config;


import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@TestConfiguration
@EnableWebSecurity
public class SecurityTestConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() // Deshabilitar CSRF solo para los tests.
                .authorizeRequests()
                .antMatchers("/login", "/register").permitAll() // Permitir acceso sin autenticación en los endpoints de prueba.
                .anyRequest().authenticated();
    }
}