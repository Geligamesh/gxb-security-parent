package com.gxb.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("permission")
@Controller
public class SysPermissionController {

    private static final String HTML_PREFIX = "system/permission/";

    @GetMapping({"","/"})
    public String permission() {
        return HTML_PREFIX + "permission-list";
    }
}
