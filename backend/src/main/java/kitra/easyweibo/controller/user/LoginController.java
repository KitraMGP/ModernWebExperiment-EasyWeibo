package kitra.easyweibo.controller.user;

import cn.dev33.satoken.stp.StpUtil;
import jakarta.validation.Valid;
import kitra.easyweibo.annotation.UserRestController;
import kitra.easyweibo.dto.ApiResponse;
import kitra.easyweibo.dto.user.LoginRequest;
import kitra.easyweibo.entity.UserEntity;
import kitra.easyweibo.service.UserService;
import kitra.easyweibo.util.ApiUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@UserRestController
public class LoginController {
    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ApiResponse<Object> login(@RequestBody @Valid LoginRequest loginRequest) {
        UserEntity userEntity = userService.login(loginRequest.username(), loginRequest.password());
        StpUtil.login(userEntity.getId());
        return ApiUtil.successfulResponse(null);
    }
}
