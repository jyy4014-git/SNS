package boardproject.sns.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AlarmType {
    NEW_COMMENT_ON_POST("새로운 댓글!"),
    NEW_LIKE_ON_POST("새로운 좋아요!");

    private final String alarmText;
}
