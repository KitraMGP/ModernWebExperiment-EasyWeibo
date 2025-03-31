package kitra.easyweibo.exception;

public class LoginFailedException extends ApiException {
    public LoginFailedException() {
        super(4005, "用户名或密码错误");
    }
}
