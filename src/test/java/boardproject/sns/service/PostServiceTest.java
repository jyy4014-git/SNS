package boardproject.sns.service;

import boardproject.sns.entity.PostEntity;
import boardproject.sns.entity.UserEntity;
import boardproject.sns.exception.ErrorCode;
import boardproject.sns.exception.SnsException;
import boardproject.sns.fixture.PostEntityFixture;
import boardproject.sns.fixture.UserEntityFixture;
import boardproject.sns.repository.PostRepository;
import boardproject.sns.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    void 포스트정상작동(){
        String title = "타이블";
        String body = "body";
        String userName = "userName";

//                mocking
        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(postRepository.save(any())).thenReturn(mock(PostEntity.class));
        Assertions.assertDoesNotThrow(() -> postService.create(title, body,userName));

    }

    @Test
    void 포스트작성시요청한유저가존재하지않음(){
        String title = "타이블";
        String body = "body";
        String userName = "userName";

//                mocking
        when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(postRepository.save(any())).thenReturn(mock(PostEntity.class));
        SnsException e = assertThrows(SnsException.class, () -> postService.create(title, body, userName));

        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());


    }

    @Test
    void 포스트수정정상작동(){
        String title = "타이블";
        String body = "body";
        String userName = "userName";
        Integer postId = 1;

//                mocking
        PostEntity postEntity = PostEntityFixture.get(userName, postId,1);
        UserEntity userEntity = postEntity.getUser();

        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postRepository.findById(postId)).thenReturn(Optional.of(postEntity));
        when(postRepository.saveAndFlush(any())).thenReturn(postEntity);

        Assertions.assertDoesNotThrow(() -> postService.modify(title, body, userName, postId));

    }

    @Test
    void 게시물수정시게시물없는경우(){
        String title = "타이블";
        String body = "body";
        String userName = "userName";
        Integer postId = 1;


//                mocking
        PostEntity postEntity = PostEntityFixture.get(userName, postId, 1);
        UserEntity userEntity = postEntity.getUser();

        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postRepository.findById(postId)).thenReturn(Optional.empty());
        SnsException e = assertThrows(SnsException.class, () -> postService.modify(title, body, userName, postId));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());

    }

    @Test
    void 게시물수정시권한이없는경우(){
        String title = "타이블";
        String body = "body";
        String userName = "userName";
        Integer postId = 1;

//                mocking
        PostEntity postEntity = PostEntityFixture.get(userName, postId,1);
        UserEntity writer = UserEntityFixture.get("userName1", "1234", 1);

        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(writer));
        when(postRepository.findById(postId)).thenReturn(Optional.of(postEntity));
        SnsException e = assertThrows(SnsException.class, () -> postService.modify(title, body, userName, postId));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());

    }

    @Test
    void 포스트삭제정상작동(){

        String userName = "userName";
        Integer postId = 1;

//                mocking
        PostEntity postEntity = PostEntityFixture.get(userName, postId,1);
        UserEntity userEntity = postEntity.getUser();

        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        Assertions.assertDoesNotThrow(() -> postService.delete(userName, 1));

    }

    @Test
    void 게시물삭제시게시물없는경우(){
        String userName = "userName";
        Integer postId = 1;


//                mocking
        PostEntity postEntity = PostEntityFixture.get(userName, postId, 1);
        UserEntity userEntity = postEntity.getUser();

        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postRepository.findById(postId)).thenReturn(Optional.empty());
        SnsException e = assertThrows(SnsException.class, () -> postService.delete(userName, 1));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());

    }

    @Test
    void 게시물삭제시권한이없는경우(){
        String userName = "userName";
        Integer postId = 1;

//                mocking
        PostEntity postEntity = PostEntityFixture.get(userName, postId,1);
        UserEntity writer = UserEntityFixture.get("userName1", "1234", 1);

        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(writer));
        when(postRepository.findById(postId)).thenReturn(Optional.of(postEntity));
        SnsException e = assertThrows(SnsException.class, () -> postService.delete(userName, 1));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());

    }

    @Test
    void 피드목록요청성공(){

        Pageable pageable = mock(Pageable.class);
        when(postRepository.findAll(pageable)).thenReturn(Page.empty());
        Assertions.assertDoesNotThrow(() -> postService.list(pageable));
    }

    @Test
    void 내피드목록요청성공(){
        Pageable pageable = mock(Pageable.class);
        UserEntity userEntity1 = UserEntityFixture.get("userName", "1234", 1);when(userRepository.findByUserName(any())).thenReturn(Optional.of(userEntity1));
        when(postRepository.findAllByUser(any(), pageable)).thenReturn(Page.empty());
        Assertions.assertDoesNotThrow(() -> postService.my(userEntity1.getUserName(),pageable));
    }
}
