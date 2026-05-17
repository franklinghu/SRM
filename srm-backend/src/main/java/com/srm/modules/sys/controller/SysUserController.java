package com.srm.modules.sys.controller;

import com.srm.common.result.Result;
import com.srm.modules.sys.entity.SysUser;
import com.srm.modules.sys.service.SysUserService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService userService;

    @PostMapping("/login")
    public Result<?> login(@Valid @RequestBody LoginRequest request) {
        SysUser user = userService.login(request.getUsername(), request.getPassword());
        String token = userService.generateToken(user);
        return Result.success(new LoginResponse(token, user));
    }

    @GetMapping("/userinfo")
    public Result<?> getUserInfo() {
        SysUser user = userService.getUserInfo();
        return Result.success(user);
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    @lombok.AllArgsConstructor
    public static class LoginResponse {
        private String token;
        private SysUser user;
    }
}
