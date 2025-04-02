package kitra.easyweibo.dto.post;

import jakarta.validation.constraints.NotNull;

public record DeletePostRequest(@NotNull Integer postId) {
}
