package kitra.easyweibo.exception;

public class ContentTooLongException extends ApiException {
    public ContentTooLongException() {
        super(4006, "内容长度超出限制");
    }
}
