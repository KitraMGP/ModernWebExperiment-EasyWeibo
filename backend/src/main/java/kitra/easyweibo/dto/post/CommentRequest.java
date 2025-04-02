package kitra.easyweibo.dto.post;

import jakarta.validation.constraints.NotNull;

public record CommentRequest(@NotNull Integer postId, @NotNull String content) {
}
