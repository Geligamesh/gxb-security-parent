package com.gxb.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("role")
@Controller
@Slf4j
public class SysRoleController {

    private static final String HTML_PREFIX = "system/role/";

    @PreAuthorize("hasAuthority('sys:role')")
    @GetMapping({"","/"})
    public String role() {
        return HTML_PREFIX + "role-list";
    }
}
