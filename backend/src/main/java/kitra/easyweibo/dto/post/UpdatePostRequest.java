package kitra.easyweibo.dto.post;

import jakarta.validation.constraints.NotNull;

public record UpdatePostRequest(@NotNull Integer postId, @NotNull String content, @NotNull String[] images) {
}
