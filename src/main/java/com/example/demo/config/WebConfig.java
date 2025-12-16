package com.example.demo.config;

import com.example.demo.security.JwtRequestFilter;
import com.example.demo.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private CustomUserDetailsService userDetailsService;

  @Autowired
  private JwtRequestFilter jwtRequestFilter;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
            .csrf().disable()
            .authorizeRequests()
            // Endpoint pubblici
            .antMatchers("/public/**", "/api/auth/**", "/h2-console/**").permitAll()
            // Endpoint protetti per ruoli
            .antMatchers("/api/protected/admin/**").hasRole("ADMIN")
            .antMatchers("/api/protected/user/**", "/api/protected/info").hasAnyRole("USER", "ADMIN")
            // Tutti gli altri richiedono autenticazione
            .anyRequest().authenticated()
            .and()
            // Disabilita sessioni (stateless per JWT)
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    // Aggiungi filtro JWT prima del filtro di autenticazione standard
    http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    // Permetti H2 console (solo per sviluppo)
    http.headers().frameOptions().disable();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
  }
}