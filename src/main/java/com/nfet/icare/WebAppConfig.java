package com.nfet.icare;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter {
	@Override  
    public void addInterceptors(InterceptorRegistry registry) {  
        //注册自定义拦截器，添加拦截路径
        registry.addInterceptor(new InterceptorConfig()).addPathPatterns("/page/callcenter**")
        .addPathPatterns("/page/companydata").addPathPatterns("/page/devicetable")
        .addPathPatterns("/page/marketdepartment").addPathPatterns("/page/repairdata")
        .addPathPatterns("/page/trafficstatistics").addPathPatterns("/page/warratydata")
        .addPathPatterns("/mgr/**").excludePathPatterns("/mgr/login");  
    }
}
