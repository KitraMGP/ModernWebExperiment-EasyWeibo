package kitra.easyweibo.exception;

public class BadPasswordException extends ApiException {
    public BadPasswordException() {
        super(40004, "密码错误");
    }
}
