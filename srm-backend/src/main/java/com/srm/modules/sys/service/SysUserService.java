package com.srm.modules.sys.service;

import com.srm.modules.sys.entity.SysUser;

public interface SysUserService {
    SysUser login(String username, String password);
    String generateToken(SysUser user);
    SysUser getUserInfo();
}
