package kitra.easyweibo.controller.post;

import cn.dev33.satoken.stp.StpUtil;
import jakarta.validation.Valid;
import kitra.easyweibo.annotation.PostRestController;
import kitra.easyweibo.dto.ApiResponse;
import kitra.easyweibo.dto.post.DeleteCommentRequest;
import kitra.easyweibo.service.PostService;
import kitra.easyweibo.util.ApiUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@PostRestController
public class DeleteCommentController {
    private final PostService postService;

    public DeleteCommentController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/deleteComment")
    public ApiResponse<Object> deleteComment(@RequestBody @Valid DeleteCommentRequest request) {
        if(!StpUtil.isLogin()) {
            return ApiUtil.unauthorizedResponse();
        }
        postService.deleteComment(StpUtil.getLoginIdAsInt(), request.id());
        return ApiUtil.successfulResponse(null);
    }
}
