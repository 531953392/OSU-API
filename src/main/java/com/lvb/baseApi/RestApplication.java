package com.lvb.baseApi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * SpringBoot启动类
 */
@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.lvb"})
//@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
//@MapperScan("com.lvb.baseApi.**.dao")
public class RestApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(RestApplication.class);
    }


    /**
     * 主方法
     *
     * @param args args
     */
    public static void main(String[] args) {
        SpringApplication.run(RestApplication.class, args);
        log.info("-------api启动成功------");

    }

}
