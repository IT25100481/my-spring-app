package com.example.my_spring_app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Enable CORS and Disable CSRF
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())

                // 2. Set Session Management to Stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Configure URL Authorization
                .authorizeHttpRequests(auth -> auth
                        // Publicly accessible HTML pages
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/login.html",
                                "/register.html",
                                "/vendors.html",
                                "/gallery.html",
                                "/hotels.html",
                                "/about.html",
                                "/inquiry.html",
                                "/vendor-packages.html",
                                "/vendor-dashboard.html",
                                "/vendor-payments.html",
                                "/vendor-bookings.html",
                                "/vendor-profile.html",
                                "/vendor-services.html",
                                "/vendor-reviews.html",
                                "/vendor-messages.html",
                                "/vendor-notifications.html",
                                "/vendor-analytics.html",
                                "/vendor-availability.html",
                                "/vendor-dashboard-partd.html"
                        ).permitAll()

                        // Public API paths
                        .requestMatchers("/api/auth/**", "/api/public/**").permitAll()
                        .requestMatchers("/api/packages/**").permitAll()

                        // Static resources
                        .requestMatchers("/static/**", "/css/**", "/js/**", "/images/**", "/categories.txt", "/*.css", "/*.js").permitAll()

                        // Role-based paths
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/vendor/**").hasRole("VENDOR")
                        .requestMatchers("/api/customer/**").hasRole("CUSTOMER")

                        // Secure all other paths
                        .anyRequest().authenticated()
                )

                // 4. Add JWT Filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}