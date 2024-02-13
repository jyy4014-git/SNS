package boardproject.sns.controller.response;

import boardproject.sns.model.User;
import boardproject.sns.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserJoinResponse {

    private Integer id;
    private String userName;
    private UserRole userRole;

    public static UserJoinResponse fromUser(User user){
        return new UserJoinResponse(user.getId(), user.getUsername(), user.getUserRole());
    }
}
