package com.gxb.security;

import com.gxb.web.entities.SysPermission;
import com.gxb.web.entities.SysUser;
import com.gxb.web.service.SysPermissionService;
import com.gxb.web.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 查询数据库中的用户信息
 */
@Component("customUserDetailsService")
@Slf4j
// public class CustomUserDetailsService implements UserDetailsService {
public class CustomUserDetailsService extends AbstractUserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysPermissionService sysPermissionService;

    @Override
    public SysUser findSysUser(String usernameOrMobile) {
        log.info("请求认证的用户名：{}", usernameOrMobile);
        //1.通过请求的用户名去数据库中查询用户信息
        return sysUserService.findByUsername(usernameOrMobile);
    }

    /*@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("请求认证的用户名：{}", username);
        //1.通过请求的用户名去数据库中查询用户信息
        SysUser sysUser = sysUserService.findByUsername(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户名或者密码错误");
        }
        List<SysPermission> permissions = sysPermissionService.findByUserId(sysUser.getId());
        if (CollectionUtils.isEmpty(permissions)) {
            return sysUser;
        }
        //左侧菜单，动态渲染时候用到
        sysUser.setPermissions(permissions);
        List<GrantedAuthority> authorities = Lists.newArrayList();
        permissions.forEach(sysPermission -> {
            String code = sysPermission.getCode();
            authorities.add(new SimpleGrantedAuthority(code));
        });
        sysUser.setAuthorities(authorities);
        // String password = passwordEncoder.encode("admin");
        //2.查询用户有哪一些权限
        //3.封装用户信息和权限信息
        // return new User(username,password,
        //         AuthorityUtils.commaSeparatedStringToAuthorityList( "sys:user,sys:role,sys:user:edit"));
        return sysUser;
    }*/
}
