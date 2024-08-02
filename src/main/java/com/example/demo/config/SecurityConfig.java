package com.example.demo.config;

import com.example.demo.entity.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtConfig jwtConfig;
    // 특정 HTTP 요청에 대한 웹 기반 보안 구성
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                log.info("SpringSecurity Start!!!");
                http.csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated())
                .logout((logout) -> logout
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

                http.addFilterBefore(new JwtAuthorizationFilter(jwtConfig.jwtToken()), UsernamePasswordAuthenticationFilter.class)
                    .addFilterAt(new JwtAuthenticationFilter(jwtConfig.jwtToken(), authenticationManager()), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 인증 관리자 관련 설정
    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider() throws Exception {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {//2 - AuthenticationManager 등록
        DaoAuthenticationProvider provider = daoAuthenticationProvider();//DaoAuthenticationProvider 사용
        return new ProviderManager(provider);
    }
}
