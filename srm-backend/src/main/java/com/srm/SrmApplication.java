package com.srm;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.srm.modules.supplier.service.SupplierApplicationService;
import com.srm.modules.sys.entity.SysUser;
import com.srm.modules.sys.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class SrmApplication {

    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(SrmApplication.class, args);
    }

    @Bean
    public CommandLineRunner initAdminUser(SysUserMapper userMapper) {
        return args -> {
            // 查找 admin 用户
            LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysUser::getUsername, "admin");
            SysUser admin = userMapper.selectOne(wrapper);
            
            String encodedPassword = passwordEncoder.encode("admin123");
            
            if (admin == null) {
                // 创建管理员用户
                admin = new SysUser();
                admin.setUsername("admin");
                admin.setPassword(encodedPassword);
                admin.setRealName("系统管理员");
                admin.setEmail("admin@example.com");
                admin.setStatus(1);
                admin.setTenantId(1L);
                
                userMapper.insert(admin);
                log.info("✅ 管理员用户创建成功 - 用户名: admin, 密码: admin123");
            } else {
                // 更新管理员密码
                admin.setPassword(encodedPassword);
                admin.setStatus(1);
                userMapper.updateById(admin);
                log.info("✅ 管理员密码已更新 - 用户名: admin, 密码: admin123");
            }
        };
    }

    @Bean
    public CommandLineRunner initApprovalFlow(SupplierApplicationService applicationService) {
        return args -> {
            applicationService.createDefaultApprovalFlow();
            log.info("✅ 审批流程初始化完成");
        };
    }
}
