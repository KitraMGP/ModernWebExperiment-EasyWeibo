package kitra.easyweibo.controller.post;

import cn.dev33.satoken.stp.StpUtil;
import jakarta.validation.Valid;
import kitra.easyweibo.annotation.PostRestController;
import kitra.easyweibo.dto.ApiResponse;
import kitra.easyweibo.dto.post.DeletePostRequest;
import kitra.easyweibo.service.PostService;
import kitra.easyweibo.util.ApiUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@PostRestController
public class DeletePostController {
    private final PostService postService;

    public DeletePostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/delete")
    public ApiResponse<Object> deletePost(@RequestBody @Valid DeletePostRequest request) {
        if(!StpUtil.isLogin()) {
            return ApiUtil.unauthorizedResponse();
        }
        postService.deletePost(StpUtil.getLoginIdAsInt(), request.postId());
        return ApiUtil.successfulResponse(null);
    }
}
