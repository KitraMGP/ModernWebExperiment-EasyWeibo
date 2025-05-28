package kitra.easyweibo.controller.post;

import cn.dev33.satoken.stp.StpUtil;
import kitra.easyweibo.annotation.PostRestController;
import kitra.easyweibo.dto.ApiResponse;
import kitra.easyweibo.dto.post.GetPostsResponse;
import kitra.easyweibo.dto.post.PostItem;
import kitra.easyweibo.exception.BadInputException;
import kitra.easyweibo.service.PostService;
import kitra.easyweibo.util.ApiUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        List<PostItem> postItems;
        if(lastId < -1) throw new BadInputException("lastId");
        // 获取用户登录id来查询用户是否点赞，-1代表未登录
        int userId;
        if (StpUtil.isLogin()) {
            userId = StpUtil.getLoginIdAsInt();
        } else {
            userId = -1;
        }
        // lastId为-1代表获取最新posts
        if(lastId == -1) {
            // 获取最新posts
            postItems = postService.getLatestPosts(GET_POSTS_LIMIT, userId);
        } else {
            // 获取lastId之前更早的posts
            postItems = postService.getMorePosts(lastId, GET_POSTS_LIMIT, userId);
        }
        // 判断是否没有更多帖子了
        boolean noMorePosts = postItems.size() < GET_POSTS_LIMIT;
        // 组装最终数据
        return ApiUtil.successfulResponse(new GetPostsResponse(postItems, noMorePosts));
    }
}
