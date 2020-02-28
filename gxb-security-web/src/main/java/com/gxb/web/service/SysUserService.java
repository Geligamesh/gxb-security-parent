package com.gxb.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gxb.web.entities.SysUser;

public interface SysUserService extends IService<SysUser> {

    SysUser findByUsername(String username);

    SysUser findByMobile(String mobile);
}
