package kitra.easyweibo.exception;

public class IllegalFileFormatException extends ApiException {
    public IllegalFileFormatException() {
        super(4200, "非法的文件格式");
    }
}
