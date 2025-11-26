package com.chronicare.platform.iam.infrastructure.authorization.sfs.configuration;

import com.chronicare.platform.iam.infrastructure.authorization.sfs.pipeline.BearerTokenRequestFilter;
import com.chronicare.platform.iam.infrastructure.authorization.sfs.pipeline.JwtAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final BearerTokenRequestFilter authorizationRequestFilter;
    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    public SecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService, BearerTokenRequestFilter authorizationRequestFilter, JwtAuthenticationEntryPoint unauthorizedHandler) {
        this.userDetailsService = userDetailsService;
        this.authorizationRequestFilter = authorizationRequestFilter;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.disable()) // Disable CORS for now or configure it properly if needed
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                    "/api/v1/authentication/**",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/swagger-resources/**",
                    "/webjars/**"
                ).permitAll()
                .anyRequest().authenticated()
            );
        
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authorizationRequestFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
