package boardproject.sns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {


    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰 불일치"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없다"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없다"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호 오류"),
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "중복회원입니다"),
    ALREADY_LIKED_POST(HttpStatus.CONFLICT, "해당포스트에 이미 좋아요를 눌렀다"),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "인증되지 않은 유저입니다"),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DB 에러입니다."),
    NOTIFICATION_CONNECT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알람 연결 에러"),
    ;

    private final HttpStatus status;
    private final String message;

}
