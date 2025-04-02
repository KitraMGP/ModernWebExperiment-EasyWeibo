package kitra.easyweibo.controller.post;

import cn.dev33.satoken.stp.StpUtil;
import jakarta.validation.Valid;
import kitra.easyweibo.annotation.PostRestController;
import kitra.easyweibo.dto.ApiResponse;
import kitra.easyweibo.dto.post.LikeRequest;
import kitra.easyweibo.service.PostService;
import kitra.easyweibo.util.ApiUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@PostRestController
public class LikeController {
    private final PostService postService;

    public LikeController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/like")
    public ApiResponse<Object> setLike(@RequestBody @Valid LikeRequest request) {
        if(!StpUtil.isLogin()) {
            return ApiUtil.unauthorizedResponse();
        }
        postService.likePost(StpUtil.getLoginIdAsInt(), request.postId(), request.like());
        return ApiUtil.successfulResponse(null);
    }
}
