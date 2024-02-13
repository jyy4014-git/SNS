package boardproject.sns.exception;

import boardproject.sns.controller.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
//예외들 잡아서 보내주는 역할
public class GlobalControllerAdvice {

    @ExceptionHandler(SnsException.class)
    public ResponseEntity<?> errorHandler(SnsException e) {
        log.error("에러발생 {}", e.toString());
        return ResponseEntity.status(e.getErrorCode().getStatus())
//                에러상황, 이름 중복 에러를 던지는 것
                .body(Response.error(e.getErrorCode().name()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> databaseErrorHandler(RuntimeException e) {
        log.error("Error occurs {}", e.toString());
        return ResponseEntity.status(ErrorCode.DATABASE_ERROR.getStatus())
                .body(Response.error(ErrorCode.DATABASE_ERROR.name()));
    }
}