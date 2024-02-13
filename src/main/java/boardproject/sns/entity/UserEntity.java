package boardproject.sns.entity;

import boardproject.sns.model.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
//postgre에 이미 user테이블이 있기때문에 슬래쉬를 해줘야 우리걸로 써진다
@Table(name = "\"user\"")
@Data
@SQLDelete(sql = "UPDATE \"user\" SET deleted_at = now() where id=?")
@Where(clause = "deleted_at is null") //삭제가 안된 데이터들만 보여준다
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @PrePersist
    void onPersist() {
        this.registeredAt = Timestamp.from(Instant.now());
        this.updatedAt = Timestamp.from(Instant.now());
    }

    // 서비스 단에서 DTO만 사용하도록 하기 위한 메서드
    public static UserEntity of(String userName, String password) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userName);
        userEntity.setPassword(password);
        return userEntity;
    }
}