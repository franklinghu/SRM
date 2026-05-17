package com.srm;

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
            // 检查是否已存在管理员用户
            SysUser existingAdmin = userMapper.selectById(1L);
            if (existingAdmin == null) {
                // 创建管理员用户
                SysUser admin = new SysUser();
                admin.setId(1L);
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRealName("系统管理员");
                admin.setEmail("admin@example.com");
                admin.setStatus(1);
                admin.setTenantId(1L);
                
                userMapper.insert(admin);
                log.info("✅ 管理员用户创建成功 - 用户名: admin, 密码: admin123");
            } else {
                log.info("ℹ️  管理员用户已存在，用户名: {}", existingAdmin.getUsername());
            }
        };
    }
}
