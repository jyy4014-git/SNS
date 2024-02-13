package boardproject.sns.service;

import boardproject.sns.controller.request.CommentRequest;
import boardproject.sns.entity.*;
import boardproject.sns.exception.ErrorCode;
import boardproject.sns.exception.SnsException;
import boardproject.sns.model.AlarmType;
import boardproject.sns.model.Comment;
import boardproject.sns.model.Post;
import boardproject.sns.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final AlarmRepository alarmRepository;


    @Transactional
    public void create(String title, String body, String userName) {
//        유저 찾기
        UserEntity userEntity = getUserEntityorException(userName);
//
        PostEntity postEntity = PostEntity.of(title, body, userEntity);
        postRepository.save(postEntity);
    }

    @Transactional
    public Post modify(String title, String body, String userName, Integer postId) {
        UserEntity userEntity = getUserEntity(userName);
//        유저찾기
        PostEntity postEntity = getPostEntityOrException(postId);
        if (postEntity.getUser() != userEntity) {
            throw new SnsException(ErrorCode.INVALID_PERMISSION, String.format("user %s has no permission with post %d", userName, postId));
        }

        postEntity.setTitle(title);
        postEntity.setBody(body);

        return Post.fromEntity(postRepository.saveAndFlush(postEntity));
    }

    private UserEntity getUserEntity(String userName) {
        UserEntity userEntity = getUserEntityorException(userName);
        return userEntity;
    }



    public void delete(String userName, Integer postId){
        UserEntity userEntity = getUserEntityorException(userName);

        //유저찾기
        PostEntity postEntity = getPostEntityOrException(postId);
        if (postEntity.getUser() != userEntity) {
            throw new SnsException(ErrorCode.INVALID_PERMISSION, String.format("user %s has no permission with post %d", userName, postId));
        }
        postRepository.delete(postEntity);
    }

    public Page<Post> list(Pageable pageable){
        return postRepository.findAll(pageable).map(Post::fromEntity);

    }

    public Page<Post> my(String userName, Pageable pageable){
        UserEntity userEntity = getUserEntityorException(userName);

        return postRepository.findAllByUser(userEntity, pageable).map(Post::fromEntity);

    }

    @Transactional
    public void like(Integer postId, String userName){

        PostEntity postEntity = getPostEntityOrException(postId);

        UserEntity userEntity = getUserEntityorException(userName);

//        유저가 like 한번만 누르게 체크하는 로직
        likeRepository.findByUserAndPost(userEntity, postEntity).ifPresent(it -> {
            throw new SnsException(ErrorCode.ALREADY_LIKED_POST, String.format("유저 %s 님은 이미 %d 좋아요 눌렀다", userName, postId));
        });

        likeRepository.save(LikeEntity.of(userEntity, postEntity));
        alarmRepository.save(AlarmEntity.of(postEntity.getUser(), AlarmType.NEW_LIKE_ON_POST, new AlarmArgs(userEntity.getId(), postEntity.getId())));
    }

    public int likeCount(Integer postId){

        PostEntity postEntity = getPostEntityOrException(postId);

        return likeRepository.countByPost(postEntity);

    }

    @Transactional
    public void comment(Integer postId, String userName, String comment) {
        PostEntity postEntity = getPostEntityOrException(postId);
        UserEntity userEntity = getUserEntityorException(userName);

//        댓글저장
        commentRepository.save(CommentEntity.of(userEntity, postEntity, comment));
//        포스트 유저 작성자에게, NEW_COMMENT_ON_POST알람이 간다. 로그인한 유저 정보와 target id가 들어간다
        alarmRepository.save(AlarmEntity.of(postEntity.getUser(), AlarmType.NEW_COMMENT_ON_POST, new AlarmArgs(userEntity.getId(), postEntity.getId())));

    }

    public Page<Comment> getComment(Integer postId, Pageable pageable) {

        PostEntity postEntity = getPostEntityOrException(postId);
        return commentRepository.findALLByPost(postEntity,pageable).map(Comment :: fromEntity);
    }

    private PostEntity getPostEntityOrException(Integer postId){
        return postRepository.findById(postId).orElseThrow(() ->
                new SnsException(ErrorCode.POST_NOT_FOUND, String.format("%d 찾을수 없음", postId)));
    }

    private UserEntity getUserEntityorException(String userName) {
        UserEntity userEntity = userRepository.findByUserName(userName).orElseThrow(()
                -> new SnsException(ErrorCode.USER_NOT_FOUND, String.format("%s님을 찾을수 없다", userName)));
        return userEntity;
    }



}
