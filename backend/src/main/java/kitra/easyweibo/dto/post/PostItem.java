package kitra.easyweibo.dto.post;

public record PostItem(int id, String username, String nickname, boolean hasAvatar, long time, String content,
                       String[] images, int likes, int comments, boolean isLiked) {
}
