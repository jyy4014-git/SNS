package boardproject.sns.entity;


import boardproject.sns.model.AlarmType;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;
import java.time.Instant;

@Data
@Entity
@Table(name = "\"alarm\"", indexes = {
        @Index(name = "user_id_idx", columnList = "user_id")
})
@SQLDelete(sql = "UPDATE \"alarm\" SET deleted_at = NOW() WHERE id=?")
@Where(clause = "deleted_at is null")
@NoArgsConstructor

public class AlarmEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    //알람을 받는 사람
    private UserEntity user;


    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "json")
    private AlarmArgs args;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;


    @PrePersist
    void registeredAt() {
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public static AlarmEntity of(UserEntity userEntity, AlarmType alarmType, AlarmArgs alarmArgs) {
        AlarmEntity entity = new AlarmEntity();
        entity.setUser(userEntity);
        entity.setAlarmType(alarmType);
        entity.setArgs(alarmArgs);
        return entity;
    }
}