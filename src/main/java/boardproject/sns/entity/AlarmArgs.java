package boardproject.sns.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlarmArgs {

//    알람을 발생시킨 사람
    private Integer fromUserId;
//    알람을 생성시킨 아이템Id
    private Integer targetId;
}
