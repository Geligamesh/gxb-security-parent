package com.gxb.security;

import com.gxb.web.entities.SysUser;
import com.gxb.web.service.SysPermissionService;
import com.gxb.web.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 通过手机号获取用户信息和权限资源
 */
@Slf4j
@Component("mobileUserDetailsService")
// public class MobileUserDetailsService implements UserDetailsService {
public class MobileUserDetailsService extends AbstractUserDetailsService {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysPermissionService sysPermissionService;

    @Override
    public SysUser findSysUser(String usernameOrMobile) {
        log.info("请求认证的用户名：{}", usernameOrMobile);
        //1.通过请求的用户名去数据库中查询用户信息
        return sysUserService.findByMobile(usernameOrMobile);
    }

    /*@Override
    public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {
        // 1.用过手机号查询用户信息
        log.info("请求的手机号为:{}", mobile);
        SysUser sysUser = sysUserService.findByMobile(mobile);
        if (sysUser == null) {
            throw new UsernameNotFoundException("该手机号未注册");
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
        // 2.如果有用户信息，则再获取权限信息
        // 3.封装用户信息
        // return new User("admin", "", true, true, true, true,
        //         AuthorityUtils.commaSeparatedStringToAuthorityList("ADMIN"));
    }*/
}
