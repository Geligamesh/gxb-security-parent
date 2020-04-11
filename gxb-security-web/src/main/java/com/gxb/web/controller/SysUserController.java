package com.gxb.web.controller;

import com.gxb.base.result.JsonResult;
import org.assertj.core.util.Lists;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

@RequestMapping("user")
@Controller
public class SysUserController {

    private static final String HTML_PREFIX = "system/user/";

    @PreAuthorize("hasAuthority('sys:user')")
    @GetMapping({"","/"})
    public String user() {
        return HTML_PREFIX + "user-list";
    }

    //有'sys:user:add'或者'sys:user:edit'的用户可以访问
    @PreAuthorize("hasAnyAuthority('sys:user:add','sys:user:edit')")
    @GetMapping(value = {"form"})
    public String form() {
        return HTML_PREFIX + "user-form";
    }

    //返回值的code等于200，则调用成功有权限，否则403
    @PostAuthorize("returnObject.code == 200")
    @RequestMapping("/{id}")
    @ResponseBody
    public JsonResult deleteById(@PathVariable("id") Long id) {
        return JsonResult.ok();
    }

    //过滤请求参数：filterTarget指定哪个参数，filterObject中的每个元素，如果value表达式为true数据则不会被过滤，否则就会被过滤
    @PreFilter(filterTarget = "ids",value = "filterObject > 0")
    @GetMapping("batch/{ids}") //  /user/batch/1,2,3,4,5,65
    @ResponseBody
    public JsonResult deleteByIds(@PathVariable("ids")List<Long> ids) {
        return JsonResult.ok(ids);
    }

    //过滤请求参数：filterObject中的每个元素，如果value表达式为true则对应数据会返回
    @PostFilter("!filterObject.equals(authentication.principal.username)")
    @GetMapping("list")
    @ResponseBody
    public List<String> page() {
        return Lists.newArrayList("gxb","Geligamesh","nb","admin");
    }
}
