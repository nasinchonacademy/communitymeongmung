package org.zerock.projectmeongmung.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@RequestMapping("/meongmung")
public class WebSecurityConfig {

    private final UserDetailsService userService;

    //스프링 시큐리티 기능 비활성화 *여기 꼭 다시 확인해보기*
    @Bean
    public WebSecurityCustomizer configure(){
        return (web) ->
                web
                        .ignoring()
                //.requestMatchers(toH2Console())
                .requestMatchers(new AntPathRequestMatcher("/static/**", "/resource/**")); // 특정 요청과 일치하는 url에 대한 엑세스를 설정
    }

    // 특정 HTTP 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests(auth -> auth
                        .requestMatchers("/login", "/signup1", "/signup2", "/user", "/meongmung","/mungstory", "/mungstory/mungstoryAll", "/",
                                "/api/check-duplicate/nickname", "/css/**", "/image/**", "/js/**",
                                "/api/check-duplicate", "/read-csv") // 여기서 /read-csv를 추가합니다
                        .permitAll()
                        .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/meongmung", true)
                        .failureUrl("/login?error=true"))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/meongmung")
                        .invalidateHttpSession(true))
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }


    //인증 관리자 관련 설정
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       BCryptPasswordEncoder bCryptPasswordEncoder,UserDetailsService userDetailsService)
            throws Exception  {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService); // 사용자 정보 서비스 설정
        // 이때 설정하는 서비스 클래스는 반드시 UserDetailsService를 상속받는 클래스여야한다.
        authProvider.setPasswordEncoder(bCryptPasswordEncoder); //비밀번호를 암호화하기 위한 인코더를 설정
        return new ProviderManager(authProvider);
    }

    //패스워드 인코더로 사용할 빈 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}