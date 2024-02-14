package boardproject.sns.service;

import boardproject.sns.entity.AlarmArgs;
import boardproject.sns.entity.AlarmEntity;
import boardproject.sns.entity.UserEntity;
import boardproject.sns.exception.ErrorCode;
import boardproject.sns.exception.SnsException;
import boardproject.sns.model.AlarmType;
import boardproject.sns.repository.AlarmRepository;
import boardproject.sns.repository.EmitterRepository;
import boardproject.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class AlarmService {

    private final static String ALARM_NAME = "alarm";
    private final static Long Timeout = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;
    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;

    public SseEmitter connectAlarm(Integer userId) {
        SseEmitter emitter = new SseEmitter(Timeout);
        emitterRepository.save(userId, emitter);
        emitter.onCompletion(() -> emitterRepository.delete(userId));
        emitter.onTimeout(() -> emitterRepository.delete(userId));

        try {
            log.info("send");
            emitter.send(SseEmitter.event()
                    .id("id")
                    .name(ALARM_NAME)
                    .data("연결 완료"));
        } catch (IOException exception) {
            throw new SnsException(ErrorCode.NOTIFICATION_CONNECT_ERROR);
        }
        return emitter;
    }

    //    이벤트 발생시 웹에 알람 보내주는 메소드
    public void send(AlarmType type, AlarmArgs args, Integer receiverId) {
        UserEntity userEntity = userRepository.findById(receiverId).orElseThrow(() -> new SnsException(ErrorCode.USER_NOT_FOUND));
        AlarmEntity alarmentity = alarmRepository.save(AlarmEntity.of(userEntity, type, args));
        alarmRepository.save(alarmentity);
        emitterRepository.get(receiverId).ifPresentOrElse(it -> {
                    try {
                        it.send(SseEmitter.event()
                                .id(alarmentity.getId().toString())
                                .name(ALARM_NAME)
                                .data("new alarm"));
                    } catch (IOException exception) {
                        emitterRepository.delete(receiverId);
                        throw new SnsException(ErrorCode.NOTIFICATION_CONNECT_ERROR);
                    }
                },
                () -> log.info("No emitter founded")
        );
    }



}
