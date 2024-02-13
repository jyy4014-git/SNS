package boardproject.sns.controller;

import boardproject.sns.controller.request.CommentRequest;
import boardproject.sns.controller.request.PostCreateRequest;
import boardproject.sns.controller.request.PostModifyRequest;
import boardproject.sns.controller.response.CommentResponse;
import boardproject.sns.controller.response.PostResponse;
import boardproject.sns.controller.response.Response;
import boardproject.sns.model.Post;
import boardproject.sns.service.PostService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Internal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@AllArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @PostMapping
    public Response<Void> create(@RequestBody PostCreateRequest request, Authentication authentication) {

//        필터에서 인증 진행하고 컨텍스트 해서 넣어준 이름 정보를 authentication으로 받아준다
        postService.create(request.getTitle(), request.getBody(), authentication.getName());
        return Response.success();
    }

    @PutMapping("/{postId}")
    public Response<PostResponse> modify(@PathVariable Integer postId, @RequestBody PostModifyRequest request, Authentication authentication) {

//        필터에서 인증 진행하고 컨텍스트 해서 넣어준 이름 정보를 authentication으로 받아준다
        Post post = postService.modify(request.getTitle(), request.getBody(), authentication.getName(), postId);
        return Response.success(PostResponse.fromPost(post));
    }

    @DeleteMapping("/{postId}")
    public Response<Void> delete(@PathVariable Integer postId, Authentication authentication) {
        postService.delete(authentication.getName(), postId);
        return Response.success();
    }

    @GetMapping
    public Response<Page<PostResponse>> list(Pageable pageable, Authentication authentication) {
        return Response.success(postService.list(pageable).map(PostResponse::fromPost));
    }

    @GetMapping("/my")
    public Response<Page<PostResponse>> my(Pageable pageable, Authentication authentication) {
        return Response.success(postService.my(authentication.getName(), pageable).map(PostResponse::fromPost));
    }

    @PostMapping("/{postId}/likes")
    public Response<Void> likes(@PathVariable Integer postId, Authentication authentication){
        postService.like(postId, authentication.getName());
        return Response.success();
    }

    @GetMapping("/{postId}/likes")
    public Response<Integer> likes(@PathVariable Integer postId){

        return Response.success(postService.likeCount(postId));
    }

    @PostMapping("/{postId}/comments")
    public Response<Void> comment (@PathVariable Integer postId,
                                   @RequestBody CommentRequest request,
                                   Authentication authentication) {
        postService.comment(postId, authentication.getName(), request.getComment());
        return Response.success();
    }

    @GetMapping("/{postId}/comments")
    public Response<Page<CommentResponse>> comment (@PathVariable Integer postId,
                                                    Pageable pageable,
                                                    Authentication authentication) {
        postService.getComment(postId, pageable);
        return Response.success(postService.getComment(postId, pageable).map(CommentResponse :: fromComment));
    }


}
