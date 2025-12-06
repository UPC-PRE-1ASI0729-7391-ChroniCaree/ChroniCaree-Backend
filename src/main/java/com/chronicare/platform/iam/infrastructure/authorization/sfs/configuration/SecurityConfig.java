package com.chronicare.platform.iam.infrastructure.authorization.sfs.configuration;

import com.chronicare.platform.iam.infrastructure.authorization.sfs.pipeline.BearerTokenRequestFilter;
import com.chronicare.platform.iam.infrastructure.authorization.sfs.pipeline.JwtAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Arrays;
import java.util.stream.Collectors;

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

    @Value("${cors.allowed.origins:http://localhost:4200,http://localhost:8080,http://localhost:11083,https://chornicare-backend-production.up.railway.app}")
    private String corsAllowedOrigins;

    @Value("${cors.allowed.methods:GET,POST,PUT,PATCH,DELETE,OPTIONS}")
    private String corsAllowedMethods;

    @Value("${cors.allowed.headers:*}")
    private String corsAllowedHeaders;

    @Value("${cors.allow.credentials:true}")
    private boolean corsAllowCredentials;

    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permitir múltiples orígenes del frontend (configurados por propiedad cors.allowed.origins)
        List<String> allowed = Arrays.stream(corsAllowedOrigins.split(","))
                                    .map(String::trim)
                                    .filter(s -> !s.isEmpty())
                                    .collect(Collectors.toList());
        configuration.setAllowedOrigins(allowed);
        
        List<String> methods = Arrays.stream(corsAllowedMethods.split(","))
                                    .map(String::trim)
                                    .filter(s -> !s.isEmpty())
                                    .collect(Collectors.toList());
        configuration.setAllowedMethods(methods);
        
        if ("*".equals(corsAllowedHeaders)) {
            configuration.setAllowedHeaders(List.of("*"));
        } else {
            List<String> headers = Arrays.stream(corsAllowedHeaders.split(","))
                                        .map(String::trim)
                                        .filter(s -> !s.isEmpty())
                                        .collect(Collectors.toList());
            configuration.setAllowedHeaders(headers);
        }
        
        configuration.setExposedHeaders(List.of("Authorization", "Content-Type", "X-Total-Count"));
        configuration.setAllowCredentials(corsAllowCredentials);
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
