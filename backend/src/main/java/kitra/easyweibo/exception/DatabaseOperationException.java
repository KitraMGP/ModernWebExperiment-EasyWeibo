package kitra.easyweibo.exception;

public class DatabaseOperationException extends ApiException {
    public DatabaseOperationException() {
        super(5001, "数据库操作异常");
    }
}
