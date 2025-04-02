package kitra.easyweibo.dto.post;

import jakarta.validation.constraints.NotNull;

public record LikeRequest(@NotNull Integer postId, @NotNull Boolean like) {
}
