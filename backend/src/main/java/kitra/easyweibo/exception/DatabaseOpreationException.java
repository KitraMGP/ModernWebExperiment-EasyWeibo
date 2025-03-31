package kitra.easyweibo.exception;

public class DatabaseOpreationException extends ApiException {
    public DatabaseOpreationException() {
        super(5001, "数据库操作异常");
    }
}
