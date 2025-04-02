package kitra.easyweibo.dto.post;

import jakarta.validation.constraints.NotNull;

public record NewPostRequest(@NotNull String content, @NotNull String[] images) {
}
