
package com.lvb.baseApi.common.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@Configuration
//@MapperScan(basePackages = { "com.lvb.baseApi.**.dao"}, sqlSessionFactoryRef = "sqlSessionFactory")
public class MybatisPlusConfig {
	/**
	 * SQL执行效率插件 设置 dev test 环境开启
	 * @return {@link PerformanceInterceptor}
	 */
	@Bean
	@Profile({"dev", "test"})
	public PerformanceInterceptor performanceInterceptor() {
		return new PerformanceInterceptor();
	}

	/**
	 * 分页插件分页插件
	 * @return {@link PaginationInterceptor}
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		PaginationInterceptor interceptor = new PaginationInterceptor();
		return interceptor;
	}


}
