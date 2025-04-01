package kitra.easyweibo.exception;

public class PostContentTooLongException extends ApiException {
    public PostContentTooLongException() {
        super(4006, "帖子内容长度超出限制");
    }
}
