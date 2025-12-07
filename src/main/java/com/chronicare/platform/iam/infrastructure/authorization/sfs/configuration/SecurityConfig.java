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
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
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
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permitir múltiples orígenes del frontend
        configuration.setAllowedOrigins(List.of(
            "http://localhost:4200",
            "http://localhost:4201",
            "http://127.0.0.1:4200",
            "http://127.0.0.1:4201",
            "https://chornicare-backend-production.up.railway.app",
            "https://chronicaree-frontend.onrender.com"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                // ════════════════════════════════════════════════════════════════
                // ENDPOINTS PÚBLICOS (sin autenticación)
                // ════════════════════════════════════════════════════════════════
                .requestMatchers(
                    "/",
                    "/actuator/health",
                    "/actuator/health/**",
                    "/api/v1/authentication/**",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/swagger-resources/**",
                    "/webjars/**"
                ).permitAll()
                // Permitir preflight CORS sin autenticación
                .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                // Subscription Plans - PÚBLICO (necesario ver planes antes de registrarse)
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/subscriptionPlans/**").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/subscriptionPlans").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/subscription-plans/**").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/subscription-plans").permitAll()
                // Tenant creation - PÚBLICO (para registro de hospital)
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/tenants").permitAll()
                // Doctors list - PÚBLICO (para mostrar lista de doctores disponibles)
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/doctors").permitAll()
                // Check email - PÚBLICO (para validar si email existe durante invitación)
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/users/check-email").permitAll()
                
                // ════════════════════════════════════════════════════════════════
                // ENDPOINTS PROTEGIDOS (requieren autenticación)
                // ════════════════════════════════════════════════════════════════
                .anyRequest().authenticated()
            );
        
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authorizationRequestFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
