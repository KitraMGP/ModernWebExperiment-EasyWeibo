package kitra.easyweibo.exception;

public class UserNotFoundException extends ApiException {
    public UserNotFoundException() {
        super(4001, "用户不存在");
    }
}
