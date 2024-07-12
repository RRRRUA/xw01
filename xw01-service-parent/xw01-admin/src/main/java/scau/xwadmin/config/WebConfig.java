package scau.xwadmin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import scau.xwadmin.interceptor.AllowInterceptor;
import scau.xwadmin.interceptor.JwtInterceptor;
import scau.xwadmin.interceptor.LoginInterceptor;

//@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    //注册Jwt拦截器，拦截/api/**路径
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/api/**").order(1);
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/api/admin/**").order(2);
        registry.addInterceptor(new AllowInterceptor())
                .addPathPatterns("/api/admin/**").order(3);
    }

}
