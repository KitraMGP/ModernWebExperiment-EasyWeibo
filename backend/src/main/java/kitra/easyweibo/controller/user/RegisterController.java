package kitra.easyweibo.controller.user;

import kitra.easyweibo.annotation.UserRestController;
import kitra.easyweibo.dto.ApiResponse;
import kitra.easyweibo.dto.user.RegisterRequest;
import kitra.easyweibo.service.UserService;
import kitra.easyweibo.util.ApiUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@UserRestController
public class RegisterController {
    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ApiResponse<Object> register(@RequestBody RegisterRequest registerRequest) {
        userService.register(registerRequest.username(), registerRequest.nickname(), registerRequest.password());
        return ApiUtil.successfulResponse(null);
    }
}
