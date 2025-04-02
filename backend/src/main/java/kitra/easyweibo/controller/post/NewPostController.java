package kitra.easyweibo.controller.post;

import cn.dev33.satoken.stp.StpUtil;
import jakarta.validation.Valid;
import kitra.easyweibo.annotation.PostRestController;
import kitra.easyweibo.dto.ApiResponse;
import kitra.easyweibo.dto.post.NewPostRequest;
import kitra.easyweibo.dto.post.NewPostResponse;
import kitra.easyweibo.service.PostService;
import kitra.easyweibo.util.ApiUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@PostRestController
public class NewPostController {
    private final PostService postService;

    public NewPostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/new")
    public ApiResponse<NewPostResponse> newPost(@RequestBody @Valid NewPostRequest request) {
        if(!StpUtil.isLogin()) {
            return ApiUtil.unauthorizedResponse();
        }
        int postId = postService.newPost(StpUtil.getLoginIdAsInt(), request.content(), request.images());
        return ApiUtil.successfulResponse(new NewPostResponse(postId));
    }
}
