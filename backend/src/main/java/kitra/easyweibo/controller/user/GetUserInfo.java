package kitra.easyweibo.controller.user;

import cn.dev33.satoken.stp.StpUtil;
import kitra.easyweibo.annotation.UserRestController;
import kitra.easyweibo.dto.ApiResponse;
import kitra.easyweibo.dto.user.GetUserInfoResponse;
import kitra.easyweibo.entity.UserEntity;
import kitra.easyweibo.exception.BadInputException;
import kitra.easyweibo.service.UserService;
import kitra.easyweibo.util.ApiUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@UserRestController
public class GetUserInfo {
    private final UserService userService;

    public GetUserInfo(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/info")
    public ApiResponse<GetUserInfoResponse> getUserInfo(@RequestParam(value = "user", defaultValue = "") String user) {
        UserEntity userEntity;
        // 若未指定user参数则尝试获取当前登录用户信息
        if(user.isEmpty()) {
            if(StpUtil.isLogin()) {
                userEntity = userService.getUserInfo(StpUtil.getLoginIdAsInt());
            } else {
                throw new BadInputException("需要指定用户名，或者在已登录的情况下获取当前用户信息");
            }
        } else {
            userEntity = userService.getUserInfo(user);
        }
        return ApiUtil.successfulResponse(new GetUserInfoResponse(userEntity.getId(), userEntity.getUsername(), userEntity.getNickname(), userEntity.getDescription(), userEntity.hasAvatar(), userEntity.isAdmin() ? "admin" : "user"));
    }
}
