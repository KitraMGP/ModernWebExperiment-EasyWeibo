package kitra.easyweibo.dto.user;

public record ChangePasswordRequest(String oldPassword, String newPassword) {
}
