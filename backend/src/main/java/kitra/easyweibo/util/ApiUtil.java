package kitra.easyweibo.util;

import kitra.easyweibo.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiUtil {
    /**
     * 用于快速生成“用户未登录”的返回数据
     * @return ResponseEntity，可直接作为Controller方法中的返回值
     */
    public static <T> ResponseEntity<ApiResponse<T>> unauthorizedResponse() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), null, "用户未登录"));
    }

    /**
     * 用于快速生成“成功”的返回数据
     * @param data 返回数据结构中附带的data
     * @return ResponseEntity，可直接作为Controller方法中的返回值
     */
    public static <T> ResponseEntity<ApiResponse<T>> successfulResponse(T data) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), data, "success"));
    }
}
