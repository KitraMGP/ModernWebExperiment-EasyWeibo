package kitra.easyweibo.controller.post;

import kitra.easyweibo.annotation.PostRestController;
import kitra.easyweibo.dto.ApiResponse;
import kitra.easyweibo.dto.post.CommentListItem;
import kitra.easyweibo.dto.post.CommentListResponse;
import kitra.easyweibo.exception.BadInputException;
import kitra.easyweibo.service.PostService;
import kitra.easyweibo.util.ApiUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@PostRestController
public class ListCommentsController {
    private final PostService postService;

    public ListCommentsController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/comments")
    public ApiResponse<CommentListResponse> listComments(@RequestParam(value = "post", defaultValue = "-1") int postId) {
        if (postId < 0) throw new BadInputException("无效的post参数");
        CommentListItem[] commentListItems = postService.getComments(postId);
        return ApiUtil.successfulResponse(new CommentListResponse(commentListItems));
    }
}
