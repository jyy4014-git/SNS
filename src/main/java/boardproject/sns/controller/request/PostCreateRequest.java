package boardproject.sns.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class PostCreateRequest {

    private String title;
    private String body;

}
