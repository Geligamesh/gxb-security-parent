package com.gxb.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("role")
@Controller
public class SysRoleController {

    private static final String HTML_PREFIX = "system/role/";

    @GetMapping({"","/"})
    public String role() {
        return HTML_PREFIX + "role-list";
    }
}
