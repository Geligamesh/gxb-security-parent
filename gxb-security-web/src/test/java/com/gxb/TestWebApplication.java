package com.gxb;

import com.gxb.web.entities.SysPermission;
import com.gxb.web.entities.SysUser;
import com.gxb.web.service.SysPermissionService;
import com.gxb.web.service.SysUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestWebApplication {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysPermissionService sysPermissionService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testSysUser() {
        List<SysUser> list = sysUserService.list();
        System.out.println("list:" + list);
    }

    @Test
    public void testFindByUsername() {
        SysUser admin = sysUserService.findByUsername("admin");
        System.out.println(admin);
    }

    @Test
    public void testSysPer() {
        List<SysPermission> permissions = sysPermissionService.findByUserId(99l);
        System.out.println("size:" + permissions.size());
    }

    @Test
    public void encode() {
        String password = "admin";
        String encode = passwordEncoder.encode(password);
        System.out.println(encode);
    }
}
