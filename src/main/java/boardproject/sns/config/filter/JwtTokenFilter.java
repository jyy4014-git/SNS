package boardproject.sns.config.filter;

import boardproject.sns.model.User;
import boardproject.sns.service.UserService;
import boardproject.sns.util.JwtTokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
//매번 필터 씌울거니까 OncePerRequestFilter상속한다
public class JwtTokenFilter extends OncePerRequestFilter {


    private final UserService userService;

    @Override //request가 들어오면 인증 수행할수 있는 작업
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //헤더
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(header == null || !header.startsWith("Bearer ")){
            log.error("헤더 가져오는 도중 에러발생");
            filterChain.doFilter(request, response);
            return;
        }

        try {
//            베어럴은 스페이스로 구분되있고, 첫번째에 토큰값이 들어있다.
            final String token = header.split(" ")[1].trim();


//            토큰 체크
            String key = "testSecretKey20230327testSecretKey20230327testSecretKey2023032";

            if(JwtTokenUtils.isTokenExpired(token, key)){
                log.error("토큰 만료시간이 지났습니다");
                filterChain.doFilter(request, response);
                return;
            }

//            유저가 유효한지 체크
            String userName = JwtTokenUtils.getUsername(token, key);

//            토큰에서 username 추출
            User user = userService.loadUserByUserName(userName);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities());

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }catch (RuntimeException e){
            log.error("인증중 에러 {}", e.toString());
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);

    }
}
