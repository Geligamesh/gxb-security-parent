package com.gxb.web.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SysUser implements UserDetails {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    /**
     * 密码需要通过加密后存储
     */
    private String password;
    /**
     * 帐户是否有效：1 未过期，0已过期
     * 1 true
     * 0 false
     */
    private boolean isAccountNonExpired = true;
    private boolean isAccountNonLocked = true;
    private boolean isCredentialsNonExpired = true;
    private boolean isEnabled = true;

    @TableField(exist = false)
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * 它不是sys_user表中的属性，所以要进行标识，不然mybatis-plus会报错
     */
    private String nickName;
    private String mobile;
    private String email;
    private String createDate;
    private String updateDate;

    /**
     * 拥有角色集合
     */
    @TableField(exist = false)
    private List<SysRole> roleList = Lists.newArrayList();

    /**
     * 获取所有角色id
     */
    @TableField(exist = false)
    private List<Long> roleIds = Lists.newArrayList();
    public List<Long> getRoleIds() {
        if(CollectionUtils.isNotEmpty(roleList)) {
            roleIds = Lists.newArrayList();
            for(SysRole role : roleList) {
                roleIds.add(role.getId());
            }
        }
        return roleIds;
    }

    /**
     * 封装当前用户拥有的权限资源对象
     */
    @TableField(exist = false)
    private List<SysPermission> permissions = Lists.newArrayList();
}
