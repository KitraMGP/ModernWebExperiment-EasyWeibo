package kitra.easyweibo.controller;

import cn.dev33.satoken.stp.StpUtil;
import kitra.easyweibo.dto.ApiResponse;
import kitra.easyweibo.util.ApiUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {

    @GetMapping("/logout")
    public ResponseEntity<ApiResponse<Object>> logout() {
        if(StpUtil.isLogin()) {
            StpUtil.logout();
            return ApiUtil.successfulResponse(null);
        }
        return ApiUtil.unauthorizedResponse();
    }
}
