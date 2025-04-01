package kitra.easyweibo.exception;

public class PostNotFoundException extends ApiException {
    public PostNotFoundException() {
        super(4008, "帖子不存在");
    }
}
