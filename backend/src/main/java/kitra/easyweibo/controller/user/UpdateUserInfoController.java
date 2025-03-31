package kitra.easyweibo.controller.user;

import cn.dev33.satoken.stp.StpUtil;
import kitra.easyweibo.annotation.UserRestController;
import kitra.easyweibo.dto.ApiResponse;
import kitra.easyweibo.dto.user.UpdateUserInfoRequest;
import kitra.easyweibo.service.UserService;
import kitra.easyweibo.util.ApiUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@UserRestController
public class UpdateUserInfoController {
    private final UserService userService;

    public UpdateUserInfoController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/updateUserInfo")
    public ApiResponse<Object> updateUserInfo(@RequestBody UpdateUserInfoRequest request) {
        if (!StpUtil.isLogin()) {
            return ApiUtil.unauthorizedResponse();
        }
        userService.updateUserInfo(StpUtil.getLoginIdAsInt(), request.username(), request.nickname(), request.description());
        return ApiUtil.successfulResponse(null);
    }
}
