package kitra.easyweibo.controller.user;

import cn.dev33.satoken.stp.StpUtil;
import kitra.easyweibo.annotation.UserRestController;
import kitra.easyweibo.dto.ApiResponse;
import kitra.easyweibo.util.ApiUtil;
import org.springframework.web.bind.annotation.GetMapping;

@UserRestController
public class LogoutController {

    @GetMapping("/logout")
    public ApiResponse<Object> logout() {
        if(StpUtil.isLogin()) {
            StpUtil.logout();
            return ApiUtil.successfulResponse(null);
        }
        return ApiUtil.unauthorizedResponse();
    }
}
