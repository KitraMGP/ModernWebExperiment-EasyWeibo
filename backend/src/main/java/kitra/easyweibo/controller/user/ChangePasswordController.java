package kitra.easyweibo.controller.user;

import cn.dev33.satoken.stp.StpUtil;
import kitra.easyweibo.annotation.UserRestController;
import kitra.easyweibo.dto.ApiResponse;
import kitra.easyweibo.dto.user.ChangePasswordRequest;
import kitra.easyweibo.service.UserService;
import kitra.easyweibo.util.ApiUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@UserRestController
public class ChangePasswordController {
    private final UserService userService;

    public ChangePasswordController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/changePassword")
    public ApiResponse<Object> changePassword(@RequestBody ChangePasswordRequest request) {
        if(!StpUtil.isLogin()) {
            return ApiUtil.unauthorizedResponse();
        }
        userService.changePassword(StpUtil.getLoginIdAsInt(), request.oldPassword(), request.newPassword());
        return ApiUtil.successfulResponse(null);
    }
}
