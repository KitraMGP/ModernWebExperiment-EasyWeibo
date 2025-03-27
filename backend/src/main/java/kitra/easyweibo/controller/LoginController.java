package kitra.easyweibo.controller;

import cn.dev33.satoken.stp.StpUtil;
import kitra.easyweibo.dto.ApiResponse;
import kitra.easyweibo.dto.LoginRequest;
import kitra.easyweibo.dto.LoginResponse;
import kitra.easyweibo.entity.UserEntity;
import kitra.easyweibo.service.UserService;
import kitra.easyweibo.util.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        UserEntity userEntity = userService.login(loginRequest.userId(), loginRequest.password());
        if(userEntity != null) {
            StpUtil.login(loginRequest.userId());
            LoginResponse loginResponse = new LoginResponse(loginRequest.userId(), userEntity.getNickname());
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), loginResponse, "登录成功"));
        }
        return ApiUtil.failedResponse(HttpStatus.UNAUTHORIZED.value(), "用户名或密码错误");
    }
}
