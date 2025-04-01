package kitra.easyweibo.exception;

public class CommentNotFoundException extends ApiException {
    public CommentNotFoundException() {
        super(4009, "评论不存在");
    }
}
