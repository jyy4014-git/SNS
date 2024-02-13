package boardproject.sns.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginResponse {
//    정상 로그인시 토큰 값 반환
    private String token;
}