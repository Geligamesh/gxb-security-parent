package com.gxb.security;

import com.gxb.web.entities.SysPermission;
import com.gxb.web.entities.SysUser;
import com.gxb.web.service.SysPermissionService;
import com.gxb.web.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public abstract class AbstractUserDetailsService implements UserDetailsService {

    @Autowired
    private SysPermissionService sysPermissionService;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 这个方法交给子类去实现它，查询用户信息
     * @param usernameOrMobile 用户名或者手机号
     * @return
     */
    public abstract SysUser findSysUser(String usernameOrMobile);

    @Override
    public UserDetails loadUserByUsername(String usernameOrMobile) throws UsernameNotFoundException {
        SysUser sysUser = this.findSysUser(usernameOrMobile);
        this.findSysPermission(sysUser);
        return sysUser;
    }

    public void findSysPermission(SysUser sysUser) {
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户名或者密码错误");
        }
        List<SysPermission> permissions = sysPermissionService.findByUserId(sysUser.getId());
        if (CollectionUtils.isEmpty(permissions)) {
            return ;
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
        //         AuthorityUtils.commaSeparatedStringToAuthorityList("sys:user,sys:role,sys:user:edit"));
        return;
    }
}
