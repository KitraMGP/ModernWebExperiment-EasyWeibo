package kitra.easyweibo.controller;

import kitra.easyweibo.dto.ApiResponse;
import kitra.easyweibo.dto.RegisterRequest;
import kitra.easyweibo.service.UserService;
import kitra.easyweibo.util.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {
    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Object>> register(@RequestBody RegisterRequest registerRequest) {
        if(userService.register(registerRequest.username(), registerRequest.nickname(), registerRequest.password())) {
            return ApiUtil.successfulResponse(null);
        }
        return ApiUtil.failedResponse(HttpStatus.UNAUTHORIZED.value(), "注册失败");
    }
}
