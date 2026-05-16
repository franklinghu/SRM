package com.srm.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.srm.modules.sys.entity.SysUser;
import com.srm.modules.sys.mapper.SysUserMapper;
import com.srm.modules.sys.security.JwtTokenProvider;
import com.srm.modules.sys.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {

    private final SysUserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public SysUser login(String username, String password) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        SysUser user = userMapper.selectOne(wrapper);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("用户名或密码错误");
        }

        if (user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用");
        }

        return user;
    }

    @Override
    public String generateToken(SysUser user) {
        return jwtTokenProvider.generateToken(user.getId(), user.getUsername());
    }

    @Override
    public SysUser getUserInfo() {
        // 从SecurityContext获取当前用户
        return null;
    }
}
