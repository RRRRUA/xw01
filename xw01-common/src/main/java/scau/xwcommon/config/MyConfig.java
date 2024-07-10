package scau.xwcommon.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MyConfig {
//    @Bean
//    //1、@LoadBalanced注解赋予RestTemplate负载均衡的能力；2、会去nacos根据服务转换成ip
//    @LoadBalanced
//    public RestTemplate restTemplate() {
//        System.out.println("restTemplate");
//        return new RestTemplate();
//    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        System.out.println("MybatisPlusInterceptor");
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL)); // 如果配置多个插件, 切记分页最后添加
        // 如果有多数据源可以不配具体类型, 否则都建议配上具体的 DbType
        return interceptor;
    }
}
