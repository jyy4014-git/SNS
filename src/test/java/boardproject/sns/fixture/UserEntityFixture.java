package boardproject.sns.fixture;

import boardproject.sns.entity.UserEntity;

//테스트용 가짜 엔티티
public class UserEntityFixture {

    public static UserEntity get(String userName, String password, Integer userId) {
        UserEntity result = new UserEntity();
        result.setId(userId);
        result.setUserName(userName);
        result.setPassword(password);

        return result;
    }
}
