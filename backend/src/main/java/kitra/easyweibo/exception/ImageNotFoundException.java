package kitra.easyweibo.exception;

public class ImageNotFoundException extends ApiException {
    public ImageNotFoundException(String message) {
        super(4203, "图片不存在：" + message);
    }
}
