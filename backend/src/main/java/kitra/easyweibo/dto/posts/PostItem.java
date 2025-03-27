package kitra.easyweibo.dto.posts;

public record PostItem(int id, long time, String user, String nickname, String content, int likes) {
}
