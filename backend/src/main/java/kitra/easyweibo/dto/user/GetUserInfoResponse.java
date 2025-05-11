package kitra.easyweibo.dto.user;

public record GetUserInfoResponse(int userId, String username, String nickname, String description, boolean hasAvatar, String role) {
}
