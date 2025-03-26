package kitra.easyweibo.dto;

public record ApiResponse<T>(int code, T data, String msg) {
}
