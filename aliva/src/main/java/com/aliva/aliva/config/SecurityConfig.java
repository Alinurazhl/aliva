package com.aliva.aliva.config;

import com.aliva.aliva.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth

                        // Публичные страницы
                        .requestMatchers(
                                "/",
                                "/products",
                                "/products/**",
                                "/categories",
                                "/categories/**",
                                "/register",
                                "/login",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()

                        // Только ADMIN
                        .requestMatchers("/admin/**")
                        .hasRole("ADMIN")

                        // Остальные только после входа
                        .anyRequest()
                        .authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")

                        // ВАЖНО:
                        // делаем собственную страницу после входа
                        .successHandler((request, response, authentication) -> {

                            boolean isAdmin = authentication.getAuthorities()
                                    .stream()
                                    .anyMatch(
                                            authority ->
                                                    authority.getAuthority()
                                                            .equals("ROLE_ADMIN")
                                    );

                            if (isAdmin) {
                                response.sendRedirect("/admin");
                            } else {
                                response.sendRedirect("/products");
                            }
                        })

                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .permitAll()
                )

                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}