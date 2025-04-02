package kitra.easyweibo.exception;

public class PostImageCountExceedsLimitException extends ApiException {
    public PostImageCountExceedsLimitException() {
        super(4202, "帖子图片数量超出限制");
    }
}
