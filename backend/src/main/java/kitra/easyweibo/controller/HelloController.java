package kitra.easyweibo.controller;

import cn.dev33.satoken.stp.StpUtil;
import kitra.easyweibo.dto.ApiResponse;
import kitra.easyweibo.dto.Greeting;
import kitra.easyweibo.util.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public ResponseEntity<ApiResponse<Greeting>> hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        if(StpUtil.isLogin()) {
            Greeting greeting = new Greeting("Hello", name);
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), greeting, "success"));
        } else {
            return ApiUtil.unauthorizedResponse();
        }
    }
}
