package com.gxb.web.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//开启事务管理
@EnableTransactionManagement
//扫描mapper接口
@MapperScan(basePackages = {"com.gxb.web.mapper"})
@Configuration
public class MybatisPlusConfig {

    /**
     * 分页插件
     * @return
     */
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
