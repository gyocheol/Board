package com.board.config;

import com.board.util.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
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
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()      // http basic 인증 방법 비활성화
                .formLogin().disable()      // form 기반 로그인 비활성화
                .csrf().disable()           // csrf 관련 설정 비활성화
                .cors().disable()           // cors 관련 설정 비활성화
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)     // 무상태 인증 방식인 JWT를 생성하고 서버에 보낼 때 세션을 생성하지 않도록 하는 것! JWT를 사용할 때 필수로 사용해야한다.
                .and()
                .authorizeRequests()
                .anyRequest().permitAll()  // 현재는 모든 request 요청 혀용 TODO: 추후 권한 별 허용 예정
                .and()
                .addFilterBefore(jwtRequestFilter, BasicAuthenticationFilter.class);
        return http.build();
    }

    /**
     * 패스워드 인코더 설정
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * login 시 사용자 유효성 검사 - 확장 가능성 높음 (JWT Filter로 직접 처리하면 아래 코드 삭제)
     * @param authenticationConfiguration
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
