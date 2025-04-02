package kitra.easyweibo.controller.post;

import cn.dev33.satoken.stp.StpUtil;
import jakarta.validation.Valid;
import kitra.easyweibo.annotation.PostRestController;
import kitra.easyweibo.dto.ApiResponse;
import kitra.easyweibo.dto.post.CommentRequest;
import kitra.easyweibo.dto.post.CommentResponse;
import kitra.easyweibo.service.PostService;
import kitra.easyweibo.util.ApiUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@PostRestController
public class NewCommentController {
    private final PostService postService;

    public NewCommentController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/comment")
    public ApiResponse<CommentResponse> newComment(@RequestBody @Valid CommentRequest request) {
        if(!StpUtil.isLogin()) {
            return ApiUtil.unauthorizedResponse();
        }
        int commentId = postService.addComment(StpUtil.getLoginIdAsInt(), request.postId(), request.content());
        return ApiUtil.successfulResponse(new CommentResponse(commentId));
    }
}
