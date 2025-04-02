package kitra.easyweibo.controller.post;

import cn.dev33.satoken.stp.StpUtil;
import jakarta.validation.Valid;
import kitra.easyweibo.annotation.PostRestController;
import kitra.easyweibo.dto.ApiResponse;
import kitra.easyweibo.dto.post.UpdatePostRequest;
import kitra.easyweibo.service.PostService;
import kitra.easyweibo.util.ApiUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@PostRestController
public class UpdatePostController {
    private final PostService postService;

    public UpdatePostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/update")
    public ApiResponse<Object> updatePost(@RequestBody @Valid UpdatePostRequest request) {
        if(!StpUtil.isLogin()) {
            return ApiUtil.unauthorizedResponse();
        }
        postService.updatePost(StpUtil.getLoginIdAsInt(), request.postId(), request.content(), request.images());
        return ApiUtil.successfulResponse(null);
    }
}
