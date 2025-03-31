package kitra.easyweibo.dto.post;

public record UpdatePostRequest(int postId, String content, String[] images) {
}
