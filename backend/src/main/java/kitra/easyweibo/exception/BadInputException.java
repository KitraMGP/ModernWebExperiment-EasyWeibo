package kitra.easyweibo.exception;

public class BadInputException extends ApiException {
    public BadInputException(String reason) {
        super(4003, "输入数据不合法：" + reason);
    }
}
