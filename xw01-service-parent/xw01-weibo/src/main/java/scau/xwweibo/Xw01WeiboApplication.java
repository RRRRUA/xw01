package scau.xwweibo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

//开启feign的注解，扫描对应的包
@EnableFeignClients("scau.xwcommon")
//nacos发现服务需要的注解
//@EnableDiscoveryClient
//macos自动刷新的配置
//@RefreshScope
//默认扫描当前包及其子包
@SpringBootApplication
//覆盖了原来的扫描路径
//@ComponentScan("scau.xwcommon.config")

//正确的写法
//@ComponentScan(value = {"scau.xwcommon.config","scau.xwweibo"})
@ComponentScan(basePackages = "scau")
@MapperScan("scau.xwweibo.mapper")
@RefreshScope
public class Xw01WeiboApplication {

    public static void main(String[] args) {
        SpringApplication.run(Xw01WeiboApplication.class, args);
    }

}
