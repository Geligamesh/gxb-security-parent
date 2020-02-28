package com.gxb.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gxb.web.entities.SysPermission;
import java.util.List;

public interface SysPermissionService extends IService<SysPermission> {

    /**
     * 通过用户id查询所拥有权限
     * @param userId
     * @return
     */
    List<SysPermission> findByUserId(Long userId);

}
