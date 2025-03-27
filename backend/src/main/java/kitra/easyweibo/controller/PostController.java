package kitra.easyweibo.controller;

import kitra.easyweibo.dto.ApiResponse;
import kitra.easyweibo.dto.posts.NewPostRequest;
import kitra.easyweibo.dto.posts.NewPostResponse;
import kitra.easyweibo.dto.posts.PostItem;
import kitra.easyweibo.dto.posts.PostsResponse;
import kitra.easyweibo.entity.PostEntity;
import kitra.easyweibo.service.PostService;
import kitra.easyweibo.util.ApiUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PostController {
    private final PostService postService;
    private static final int GET_POSTS_LIMIT = 5;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<PostsResponse>> getPosts(@RequestParam(value = "lastId", defaultValue = "-1") int lastId) {
        List<PostEntity> posts;
        List<PostItem> postItems;
        // lastId为-1代表获取最新posts
        if(lastId == -1) {
            // 获取最新posts
            posts = postService.getLatestPosts(GET_POSTS_LIMIT);
        } else {
            // 获取lastId之前更早的posts
            posts = postService.getMorePosts(lastId, GET_POSTS_LIMIT);
        }
        // 转换成PostItem列表
        postItems = new ArrayList<>(posts.size());
        for(PostEntity p : posts) {
            postItems.add(new PostItem(p.getId(), p.getTime(), p.getUser().getName(), p.getUser().getNickname(), p.getContent(), p.getLikes()));
        }
        // 组装最终数据
        return ApiUtil.successfulResponse(new PostsResponse(postItems));
    }

    @PostMapping("/newpost")
    public ResponseEntity<ApiResponse<NewPostResponse>> newPost(@RequestBody NewPostRequest request) {
        int postId = postService.newPost(request.userId(), request.content());
        if(postId >= 0) {
            return ApiUtil.successfulResponse(new NewPostResponse(postId));
        }
        if(postId == PostService.POST_INVALID_USER) {
            return ApiUtil.failedResponse(200401, "用户不存在");
        }
        if(postId == PostService.POST_TOO_LONG) {
            return ApiUtil.failedResponse(200402, "帖子内容过长");
        }
        return ApiUtil.failedResponse(200402, "发生未知错误");
    }
}
