package scau.xwuser;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients("scau.xwcommon")
@MapperScan("scau.xwuser.mapper")
// 通过@RefreshScope注解来刷新配置 如果配置中心有修改，自动更新数据
@RefreshScope
@ComponentScan(basePackages = "scau")
public class Xw01UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(Xw01UserApplication.class, args);
    }

}
