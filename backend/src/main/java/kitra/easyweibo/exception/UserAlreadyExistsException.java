package kitra.easyweibo.exception;

public class UserAlreadyExistsException extends ApiException {
    public UserAlreadyExistsException() {
        super(4002, "用户已存在");
    }
}
