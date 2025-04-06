package kitra.easyweibo.dto.post;

public record CommentListItem(int id, int userId, String username, String nickname, boolean hasAvatar, long time, String content) {
}
