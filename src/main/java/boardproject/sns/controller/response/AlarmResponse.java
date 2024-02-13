package boardproject.sns.controller.response;

import boardproject.sns.entity.AlarmArgs;
import boardproject.sns.entity.AlarmEntity;
import boardproject.sns.model.Alarm;
import boardproject.sns.model.AlarmType;
import boardproject.sns.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.sql.Timestamp;


@AllArgsConstructor
@Data
public class AlarmResponse {
    private Integer id;
    private String text;
    private AlarmType alarmType;
    private AlarmArgs alarmArgs;
    private Timestamp registedAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static AlarmResponse fromAlarm(Alarm alarm){
        return new AlarmResponse(
                alarm.getId(),
                alarm.getAlarmType().getAlarmText(),
                alarm.getAlarmType(),
                alarm.getAlarmArgs(),
                alarm.getRegistedAt(),
                alarm.getUpdatedAt(),
                alarm.getDeletedAt()
        );
    }
}
