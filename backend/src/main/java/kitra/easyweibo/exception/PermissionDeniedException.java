package kitra.easyweibo.exception;

public class PermissionDeniedException extends ApiException {
    public PermissionDeniedException() {
        super(4007, "权限不足");
    }
}
