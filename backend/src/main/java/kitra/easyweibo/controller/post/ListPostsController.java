package kitra.easyweibo.controller.post;

import kitra.easyweibo.annotation.PostRestController;
import kitra.easyweibo.dto.ApiResponse;
import kitra.easyweibo.dto.post.GetPostsResponse;
import kitra.easyweibo.dto.post.PostItem;
import kitra.easyweibo.entity.PostEntity;
import kitra.easyweibo.exception.BadInputException;
import kitra.easyweibo.service.PostService;
import kitra.easyweibo.util.ApiUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@PostRestController
public class ListPostsController {
    private final PostService postService;
    private static final int GET_POSTS_LIMIT = 5;

    public ListPostsController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/list")
    public ApiResponse<GetPostsResponse> getPosts(@RequestParam(value = "lastId", defaultValue = "-1") int lastId) {
        List<PostEntity> posts;
        List<PostItem> postItems;
        if(lastId < -1) throw new BadInputException("lastId");
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
            postItems.add(new PostItem(p.getId(), p.getUser().getUsername(), p.getUser().getNickname(), false, p.getTime(), p.getContent(), new String[0], p.getLikes(), p.getComments()));
        }
        // 组装最终数据
        return ApiUtil.successfulResponse(new GetPostsResponse(postItems));
    }
}
