package scau.xwuser.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RefreshScope
public class WebConfig implements WebMvcConfigurer {

    @Value("${my.upload_dir}")
    private String uploadDir;
    @Override
    //接收到以api/images/user/开头的请求时，去服务器文件系统的uploadDir目录中寻找相应的静态资源
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("file:"+uploadDir+"/");
        registry.addResourceHandler("api/images/user/**")
                .addResourceLocations("file:"+uploadDir+"/");
    }
}
