package com.example.demo.config;

import com.example.demo.security.JwtRequestFilter;
import com.example.demo.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebConfig {

  private final CustomUserDetailsService userDetailsService;
  private final JwtRequestFilter jwtRequestFilter;

  // 🔐 Password encoder
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // 🔐 Authentication provider (sostituisce configure(AuthenticationManagerBuilder))
  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  // 🔐 AuthenticationManager (sostituisce authenticationManagerBean())
  @Bean
  public AuthenticationManager authenticationManager(
          AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();
  }

  // 🔐 Security filter chain (sostituisce configure(HttpSecurity))
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                    // Endpoint pubblici
                    .requestMatchers("/public/**", "/api/auth/**", "/h2-console/**").permitAll()
                    // Endpoint protetti per ruoli
                    .requestMatchers("/api/protected/admin/**").hasRole("ADMIN")
                    .requestMatchers("/api/protected/user/**", "/api/protected/info")
                    .hasAnyRole("USER", "ADMIN")
                    // Tutti gli altri
                    .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    // H2 console (solo dev)
    http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

    return http.build();
  }
}
