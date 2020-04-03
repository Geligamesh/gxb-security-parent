package com.gxb.web.controller;

import com.gxb.base.result.JsonResult;
import com.gxb.web.entities.SysPermission;
import com.gxb.web.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("permission")
@Controller
public class SysPermissionController {

    @Autowired
    private SysPermissionService sysPermissionService;

    private static final String HTML_PREFIX = "system/permission/";

    @PreAuthorize("hasAuthority('sys:permission')")
    @GetMapping({"","/"})
    public String permission() {
        return HTML_PREFIX + "permission-list";
    }

    @PreAuthorize("hasAuthority('sys:permission:list')")
    @PostMapping("list")
    @ResponseBody
    public JsonResult list() {
        List<SysPermission> list = sysPermissionService.list();
        return JsonResult.ok(list);
    }
}
