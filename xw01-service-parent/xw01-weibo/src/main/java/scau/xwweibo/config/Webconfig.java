package scau.xwweibo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import scau.xwcommon.interceptor.JwtInterceptor;
import scau.xwcommon.interceptor.LoginInterceptor;
@Configuration
public class Webconfig implements WebMvcConfigurer {

    @Value("${my.upload_dir}")
    private String uploadDir;

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new JwtInterceptor())
//                .addPathPatterns("/api/weibo/**").addPathPatterns("api/comment/**").order(1);
//
//    /*    registry.addInterceptor(new LoginInterceptor())
//                .addPathPatterns("/api/weibo/**").addPathPatterns("api/comment/**").order(2);*/
//
//    }


    @Override
    //接收到以api/images/user/开头的请求时，去服务器文件系统的uploadDir目录中寻找相应的静态资源
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("file:"+uploadDir+"/");
        registry.addResourceHandler("api/weibo/images/**")
                .addResourceLocations("file:"+uploadDir+"/");
    }
}
