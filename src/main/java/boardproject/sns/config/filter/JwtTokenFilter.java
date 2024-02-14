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
import java.util.List;

@Slf4j
@RequiredArgsConstructor
//매번 필터 씌울거니까 OncePerRequestFilter상속한다
public class JwtTokenFilter extends OncePerRequestFilter {


    private final UserService userService;
    private final static List<String> TokenInParamUrl = List.of("/api/v1/users/alarm/subscribe");

    @Override //request가 들어오면 인증 수행할수 있는 작업
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String token;
        //헤더

        try{
            if(TokenInParamUrl.contains(request.getRequestURI())){
//                Sse는 헤더지원이 안되기 때문에 리퀘스트 파람에서 꺼낸다.
//                alarm/subscribe의 파람안에 토큰이 있다면 값을 꺼내어 필터 실시
                token = request.getQueryString().split("=")[1].trim();
                log.info("request {} check query param", request.getRequestURI());
            }else{
                final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

                if(header == null || !header.startsWith("Bearer ")){
                    log.error("헤더 가져오는 도중 에러발생");
                    filterChain.doFilter(request, response);
                    return;
                }
                token = header.split(" ")[1].trim();

            }

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
