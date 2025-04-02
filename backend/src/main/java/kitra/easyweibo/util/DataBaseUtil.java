package kitra.easyweibo.util;

import kitra.easyweibo.exception.DatabaseOperationException;

public class DataBaseUtil {
    /**
     * 判断数据库操作返回值是否为1。若不是1，则抛出{@link DatabaseOperationException}
     *
     * @param result 数据操作方法的返回值
     */
    public static void checkResult(int result) {
        if (result != 1) {
            throw new DatabaseOperationException();
        }
    }
}
