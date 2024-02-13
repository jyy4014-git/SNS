package boardproject.sns.config;

import boardproject.sns.config.filter.JwtTokenFilter;
import boardproject.sns.exception.CustomAuthenticationEnrtyPoint;
import boardproject.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthenticConfig{

    private final UserService userService;

    //    configure(WebSecurity web) 시큐리티6 버전
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
//        /api로 시작하는 path들만 인증 갖게 한다.
        return (web) -> web.ignoring().requestMatchers("^(?!/api/).*");
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> auth
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers("/api/*/users/join", "/api/*/users/login").permitAll()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll())
                .sessionManagement((s) -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtTokenFilter(userService), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling( e -> e.authenticationEntryPoint(new CustomAuthenticationEnrtyPoint()));
        return http.build();
    }


}
