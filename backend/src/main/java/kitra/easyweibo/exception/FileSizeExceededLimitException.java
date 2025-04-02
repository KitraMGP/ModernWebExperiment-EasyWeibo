package kitra.easyweibo.exception;

public class FileSizeExceededLimitException extends ApiException {
    public FileSizeExceededLimitException(String limit) {
        super(4201, "文件长度超出限制：" + limit);
    }
}
