package kitra.easyweibo.dto.user;

public record GetUserInfoResponse(String username, String nickname, String description, boolean hasAvatar) {
}
