package boardproject.sns.consumer;

import boardproject.sns.model.event.AlarmEvent;
import boardproject.sns.service.AlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AlarmConsumer {
    private final AlarmService alarmService;

    @KafkaListener(topics = "alarm")
    public void consumeAlarm(AlarmEvent event, Acknowledgment ack){
        log.info("consume 이벤트발생 {}", event);
        alarmService.send(event.getAlarmType(),event.getArgs(), event.getReceiveUserId());
        ack.acknowledge();
    }
}
