package kitra.easyweibo.dto.post;

import jakarta.validation.constraints.NotNull;

public record DeleteCommentRequest(@NotNull Integer id) {
}
