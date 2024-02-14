package boardproject.sns.controller;

import boardproject.sns.controller.request.UserJoinRequest;
import boardproject.sns.controller.request.UserLoginRequest;
import boardproject.sns.controller.response.AlarmResponse;
import boardproject.sns.controller.response.Response;
import boardproject.sns.controller.response.UserJoinResponse;
import boardproject.sns.controller.response.UserLoginResponse;
import boardproject.sns.exception.ErrorCode;
import boardproject.sns.exception.SnsException;
import boardproject.sns.model.User;
import boardproject.sns.service.AlarmService;
import boardproject.sns.service.UserService;
import boardproject.sns.util.ClassUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AlarmService alarmService;


    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request){
        User user = userService.join(request.getName(), request.getPassword());
        return Response.success(UserJoinResponse.fromUser(user));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request){
        String token = userService.login(request.getName(), request.getPassword());

        return Response.success(new UserLoginResponse(token));
    }

    @GetMapping("/alarm")
    public Response<Page<AlarmResponse>> alarm(Pageable pageable, Authentication authentication){
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class).orElseThrow(
                () -> new SnsException(ErrorCode.DATABASE_ERROR));
        return Response.success(userService.alamList(user.getId(), pageable).map(AlarmResponse::fromAlarm));
    }

    @GetMapping("/alarm/subscribe")
    public SseEmitter subscribe(Authentication authentication){

        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class).orElseThrow(
                () -> new SnsException(ErrorCode.DATABASE_ERROR));
        return alarmService.connectAlarm(user.getId());

    }

}
