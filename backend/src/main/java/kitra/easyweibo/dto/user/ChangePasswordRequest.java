package kitra.easyweibo.dto.user;

import jakarta.validation.constraints.NotNull;

public record ChangePasswordRequest(@NotNull String oldPassword, @NotNull String newPassword) {
}
