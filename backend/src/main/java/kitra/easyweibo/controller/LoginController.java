package kitra.easyweibo.controller;

import cn.dev33.satoken.stp.StpUtil;
import kitra.easyweibo.dto.ApiResponse;
import kitra.easyweibo.dto.LoginRequest;
import kitra.easyweibo.dto.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        // TODO 实现完整登录功能
        if(loginRequest.userId().equals("kitra") || loginRequest.password().equals("123456")) {
            StpUtil.login(loginRequest.userId());
            LoginResponse loginResponse = new LoginResponse(loginRequest.userId(), "Kitra");
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), loginResponse, "登录成功"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), null, "用户名或密码错误"));
    }
}
