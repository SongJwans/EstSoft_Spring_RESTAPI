package com.estsoft.springproject.user.config;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

import com.estsoft.springproject.user.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // spring bean 등록
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final UserDetailService userDetailService;

    // 특정 요청은 스프링 Security 설정을 타지 않도록 ignore 처리
    @Bean
    public WebSecurityCustomizer ignore() {
        // lambda  표현 가능
        return WebSecurity -> WebSecurity.ignoring()
//                .requestMatchers(toH2Console())
                .requestMatchers("/static/**", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(// 3) 인증, 인가 설정
                        custom -> custom.requestMatchers("/login", "/signup", "/user").permitAll()
//                                .requestMatchers("/articles/**").hasRole("ADMIN") // ROLE_ADMIN
                                //.hasAuthority("ADMIN") ADMIN

                                .anyRequest().permitAll()
                )
//                .requestMatchers("/login","/signup","/user").permitAll()
//                .requestMatchers("/api/external").hasRole("admin") // 인가
//                .anyRequest().authenticated()

                // 4) 폼 기반 로그인 설정xqx
                .formLogin(custom -> custom.loginPage("/login")
                        .defaultSuccessUrl("/articles", true))
//                .loginPage("/login")
//                .defaultSuccessUrl("/articles")
//                .and()

                // 5) 로그아웃 설정
                .logout(custom -> custom.logoutSuccessUrl("/login")
                        .invalidateHttpSession(true))
//                .logoutSuccessUrl("/login")
//                .invalidateHttpSession(true)
//                .and()

                // 6) csrf 비 활성화
                .csrf(custom -> custom.disable())
                .build();
//                .csrf().disable()
//                .build();
    }


    // 패스워드 암호화 방식 (BCryptPasswordEncoder) 빈 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
