package boardproject.sns.producer;

import boardproject.sns.model.event.AlarmEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AlarmProducer {

    private final KafkaTemplate<Integer, AlarmEvent> kafkaTemplate;

    private String topic = "alarm";

    public void send(AlarmEvent event){
        kafkaTemplate.send(topic, event.getReceiveUserId(), event);
        log.info("카프카로보냈다");
    }
}
