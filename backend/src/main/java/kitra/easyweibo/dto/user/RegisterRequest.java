package kitra.easyweibo.dto.user;

import jakarta.validation.constraints.NotNull;

public record RegisterRequest(@NotNull String username, @NotNull String nickname, @NotNull String password) {
}
