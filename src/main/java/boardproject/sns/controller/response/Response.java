package boardproject.sns.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Response<T> {

//    에러시 코드 담는다
    private String resultCode;
//    user, 게시판, 좋아요에 대한 response type이 다르기 때문에 제네릭으로 설정
    private T result;

    public static <T> Response<T> success() {
        return new Response<T>("SUCCESS", null);
    }

    public static <T> Response<T> success(T result) {
//        성공인 경우 success 출력
        return new Response<T>("SUCCESS", result);
    }


    public static Response<Void> error(String resultCode) {
        //    에러인 경우 에러코드가 들어오고 response가 없다
        return new Response<Void>(resultCode, null);
    }

    public String toStream() {
        if (result == null) {
            return "{" +
                    "\"resultCode\":" + "\"" + resultCode + "\"," +
                    "\"result\":" + null +
                    "}";
        }
        return "{" +
                "\"resultCode\":" + "\"" + resultCode + "\"," +
                "\"result\":" + "\"" + result + "\"," +
                "}";
    }
}