package boardproject.sns.fixture;

import boardproject.sns.entity.PostEntity;
import boardproject.sns.entity.UserEntity;

//테스트용 가짜 엔티티
public class PostEntityFixture {

    public static PostEntity get(String userName, Integer postId, Integer userId) {
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setUserName(userName);

        PostEntity result = new PostEntity();
        result.setUser(user);
        result.setId(postId);
        return result;
    }
}
